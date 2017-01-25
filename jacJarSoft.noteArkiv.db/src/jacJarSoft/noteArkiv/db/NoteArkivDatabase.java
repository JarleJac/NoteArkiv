package jacJarSoft.noteArkiv.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import jacJarSoft.noteArkiv.base.NoteArkivAppInfo;
import jacJarSoft.noteArkiv.dao.SheetFileDao;
import jacJarSoft.noteArkiv.model.AccessLevel;
import jacJarSoft.noteArkiv.model.Note;
import jacJarSoft.noteArkiv.model.NoteFile;
import jacJarSoft.noteArkiv.model.NoteFileData;
import jacJarSoft.noteArkiv.model.SheetList;
import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.model.Voice;
import jacJarSoft.util.DbUtil;
import jacJarSoft.util.ExceptionUtil;
import jacJarSoft.util.PasswordUtil;
import jacJarSoft.util.Version;

public class NoteArkivDatabase {

	private static Logger logger = Logger.getLogger(NoteArkivDatabase.class.getName());
	private Version appVersion;
	private EntityManager entityManager;
	private boolean runVacuume = false;;

	public NoteArkivDatabase(EntityManager entityManager) {
		this.entityManager = entityManager;
		appVersion = NoteArkivAppInfo.getVersion();
	}

	public void verifyAndUpgradeDb(String jdbcDriverClass, String jdbcUrl) {
		DbUtil.runWithConnection(entityManager, this::upgradeDb);
		if (runVacuume)
			runDbVacuume(jdbcDriverClass, jdbcUrl);
	}
	private void runDbVacuume(String jdbcDriverClass, String jdbcUrl) {
		if (jdbcDriverClass == null || jdbcUrl == null)
			return;
		try {
			logger.info("Running vacuume...");
			Class.forName(jdbcDriverClass);
			Connection connection = DriverManager.getConnection(jdbcUrl);
			String sql = "vacuum"; //Reclaim storage
			DbUtil.execUpdateSql(connection,sql);
			logger.info("Running vacuume...Done");
		} catch (ClassNotFoundException | SQLException e) {
			logger.log(Level.SEVERE, "Excxeption trying to run vacuume!", e);
		}
		
	}

	private Void upgradeDb(Connection connection) {
		try {
			String versionStr = getDbVersion(connection);
			Version version = new Version(versionStr);
			if (version.isEarlier(appVersion))
			{
				logger.info("Upgrading database from " + version.toString() + " to " + appVersion.toString());
				if (version.getMajor() == 0 && version.getMinor() == 0) {
					createNewDatabase(connection);
				} else {
					if (version.getMajor() < 2) 
						upgradeFromVersion1(connection);
//					if (version.getMajor() == 2 & version.getMinor() < 1)
//						upgrateFromVersion2_0(connection);
				}

				updateToCurrentVersion(connection);
			} else {
				doTemporatyrUpgrades(connection);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error upgrading db", e);
			throw ExceptionUtil.getRuntimeException(e);
		}
		
		return null;
	}

	/**
	 * This method is created to be able to execute updates to db 
	 * where the version of the db is the same as the version of the app.
	 * Used during development. Delete all code from this before deployment to test/production
	 * @param connection
	 * @throws SQLException 
	 */
	private void doTemporatyrUpgrades(Connection connection) throws SQLException {
		extractFileDataIfNeeded(connection);
	}

	private void extractFileDataIfNeeded(Connection connection) throws SQLException {
		Boolean needFileExtract = false;
		try {
			needFileExtract = DbUtil.execQuery(DbUtil.createStatement(connection, "Select count(*) from note_files_data"), (rs)-> {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
				return false;
			});
		} catch (SQLException e) {
			//We ignore this, the table probably doesn't exist
		}
		
		if (needFileExtract) {
			extractFileDataToDirectory(getAllSheetFiles(), connection);
		}
	}

	private String getDbVersion(Connection connection) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS SYSTEM_INFO (" +
				 "VERSION_NO text " +
				 ")";
		DbUtil.execUpdateSql(connection,sql);
		String versionStr =
			DbUtil.execQuery(DbUtil.createStatement(connection, "Select version_no from system_info"), (rs)-> {
				String ver = "0.0.0.0";
				if (!rs.next()) {	
					DbUtil.execUpdateSql(connection,"INSERT INTO SYSTEM_INFO (VERSION_NO) VALUES(?)","0.0.0.0");
				}
				else {
					ver = rs.getString(1);
				}
				return ver;
			});
		return versionStr;
	}
	private void updateToCurrentVersion(Connection connection) throws SQLException {
		String sql = "update SYSTEM_INFO set VERSION_NO = ?";
		DbUtil.execUpdateSql(connection,sql,appVersion.toVersionString(false));
	}

	private void upgradeFromVersion1(Connection connection) throws SQLException {
		upgradeUsersFromVer1(connection);
		upgradeNotesFromVersion1(connection);

		createLists(connection);
        createListNoteLink(connection);
	}

	private void upgradeNotesFromVersion1(Connection connection) throws SQLException {
		@SuppressWarnings("unchecked")
		List<Note> sheets = (List<Note>) entityManager.createNativeQuery("select * from notes", Note.class).getResultList();
		for(Note sheet : sheets) {
			entityManager.detach(sheet);
		}
		
		String sql = "drop table notes";
		DbUtil.execUpdateSql(connection,sql);
		createNotes(connection);

		for(Note sheet : sheets) {
			entityManager.persist(sheet);
		}

	
		List<NoteFile> sheetFiles = getAllSheetFiles();
		for(NoteFile sheetFile : sheetFiles) {
			entityManager.detach(sheetFile);
		}
		sql = "drop table note_files";
		DbUtil.execUpdateSql(connection,sql);
		createNoteFiles(connection);

		for(NoteFile sheetFile : sheetFiles) {
			entityManager.persist(sheetFile);
		}
		
		extractFileDataToDirectory(sheetFiles, connection);
	}

	private List<NoteFile> getAllSheetFiles() {
		@SuppressWarnings("unchecked")
		List<NoteFile> sheetFiles = (List<NoteFile>) entityManager.createNativeQuery("select * from note_files", NoteFile.class).getResultList();
		return sheetFiles;
	}

	private void extractFileDataToDirectory(List<NoteFile> sheetFiles, Connection connection) throws SQLException {
		SheetFileDao sheetFileDao = new SheetFileDao();
		int i= 0;
		for (NoteFile sheetFile : sheetFiles) {
			if (logger.isLoggable(Level.FINE)) {
				i++;
				logger.fine("Extracting data to file for fileid " + sheetFile.getFileId() + " " + i + "/" + sheetFiles.size());
			}
//			if (sheetFile.getFileId() == 1293) {
//				logger.warning("Skipping file " + sheetFile.getName() + " for sheet " + sheetFile.getNoteId());
//				continue;
//			}
			NoteFileData noteFileData = entityManager.find(NoteFileData.class, sheetFile.getFileId());
			try {
				sheetFileDao.insertSheetFileData(sheetFile, noteFileData.getData());
			} catch (IOException e) {
				String msg = "Error saving file " + sheetFile.getName();
				logger.log(Level.SEVERE, msg, e);
				throw new RuntimeException(msg,e);
			}
		}
		String sql = "drop table note_files_data";
		DbUtil.execUpdateSql(connection,sql);
		runVacuume = true;
	}

	private void upgradeUsersFromVer1(Connection connection) throws SQLException {
		String sql = "ALTER TABLE users RENAME TO users_old;";
		DbUtil.execUpdateSql(connection,sql);
		sql = "alter table users_old add ACCESS_LEVEL text";
		DbUtil.execUpdateSql(connection,sql);
		
		sql = "update users_old set password = ?, MUST_CHANGE_PASSWORD = ?, ACCESS_LEVEL = ?";
		DbUtil.execUpdateSql(connection,sql, PasswordUtil.getPasswordMd5Hash("sommer"), true, AccessLevel.READER.name());

		sql = "update users_old set ACCESS_LEVEL = ? where is_sysadmin = 1";
		DbUtil.execUpdateSql(connection,sql, AccessLevel.SYSADMIN.name());
		sql = "update users_old set ACCESS_LEVEL = ? where is_sysadmin = 0 and is_admin = 1";
		DbUtil.execUpdateSql(connection,sql, AccessLevel.ADMIN.name());
		sql = "update users_old set ACCESS_LEVEL = ? where is_sysadmin = 0 and is_admin = 0 and is_author = 1";
		DbUtil.execUpdateSql(connection,sql, AccessLevel.AUTHOR.name());
		
		createUsersTable(connection);
		sql = "insert into users (USER_NO, USER_NAME, PASSWORD, MUST_CHANGE_PASSWORD, ACCESS_LEVEL) select USER_NO, USER_NAME, PASSWORD, MUST_CHANGE_PASSWORD, ACCESS_LEVEL from users_old";
		DbUtil.execUpdateSql(connection,sql);
		sql = "drop table users_old";
		DbUtil.execUpdateSql(connection,sql);
	}

	private void createNewDatabase(Connection connection) throws SQLException {
        createVoices(connection);
        createNotes(connection);
        createNoteFiles(connection);
        createTags(connection);
        createUsers(connection);
        createLists(connection);
        createListNoteLink(connection);
	}


	private void createLists(Connection connection) throws SQLException {
		String sql =
                "CREATE TABLE IF NOT EXISTS LISTS " + 
					"(LIST_ID integer primary key autoincrement, " +  
					"LIST_NAME text, " +
					"LIST_DATE text, " +
            		"REGISTERED_DATE text, " +
            		"REGISTERED_BY text " +
					");";
		DbUtil.execUpdateSql(connection,sql);
		initAutoIncrementSequence(connection, "LISTS");
		AddCurrentList();
	}
	private void AddCurrentList() {
		SheetList list = new SheetList();
		list.setListDateStr("1913-07-29 00:00:00");
		list.setName("Gjeldende noter");
		list.setRegisteredBy("sysadmin");
		list.setRegisteredDate(new Date());
		entityManager.persist(list);
	}

	private void createListNoteLink(Connection connection) throws SQLException {
		String sql =
                "CREATE TABLE IF NOT EXISTS LIST_NOTES " + 
					"(LIST_ID integer REFERENCES LISTS(LIST_ID) ON DELETE CASCADE, " +  
					"NOTE_ID integer  REFERENCES NOTES(NOTE_ID) ON DELETE CASCADE," +
					"PRIMARY KEY (LIST_ID, NOTE_ID)" +
					");";
		DbUtil.execUpdateSql(connection,sql);
	}

	private void createVoices(Connection connection) throws SQLException {
		String sql =
                "CREATE TABLE IF NOT EXISTS VOICES " + 
					"(VOICE_ID integer primary key autoincrement, " +  
					"VOICE_CODE text, " +
					"VOICE_NAME text " +
					");";
		DbUtil.execUpdateSql(connection,sql);
		initAutoIncrementSequence(connection, "VOICES");

        AddVoice("S", "Sopran(1)");
        AddVoice("S", "Sopran2");
        AddVoice("A", "Alt(1)");
        AddVoice("A", "Alt2");
        AddVoice("T", "Tenor(1)");
        AddVoice("T", "Tenor2");
        AddVoice("B", "Bass(1)");
        AddVoice("B", "Bass2");
        AddVoice("Ba", "Baryton");
	}
	private void createNotes(Connection connection) throws SQLException {
		String sql =
                "CREATE TABLE IF NOT EXISTS NOTES " + 
            		"(NOTE_ID integer primary key autoincrement, " +  
            		"TITLE text, " +
            		"COMPOSED_BY text, " +
            		"ARRANGED_BY text, " +
            		"TAGS text, " +
            		"VOICES text, " +
            		"DESCRIPTION text, " +
            		"REGISTERED_DATE text, " +
            		"REGISTERED_BY text " +
            		");";
		DbUtil.execUpdateSql(connection,sql);
		initAutoIncrementSequence(connection, "NOTES");
	}
	private void createNoteFiles(Connection connection) throws SQLException {
		String sql =
                "CREATE TABLE IF NOT EXISTS NOTE_FILES " + 
	                "(NOTE_FILE_ID integer primary key autoincrement, " +  
	                 "NOTE_ID integer, " + 
	                 "FILE_NAME  text, " +
	                 "DESCRIPTION text, " +
	                 "FILE_SIZE integer, " + 
	                 "REGISTERED_DATE text, " +
	                 "REGISTERED_BY text " +
	                 ");";
		DbUtil.execUpdateSql(connection,sql);
		initAutoIncrementSequence(connection, "NOTE_FILES");

//        sql = "CREATE TABLE IF NOT EXISTS NOTE_FILES_DATA " + 
//				"(NOTE_FILE_ID integer primary key, " +  
//				"FILE_DATA blob "+
//				");";
//		DbUtil.execUpdateSql(connection,sql);
	}
	private void createTags(Connection connection) throws SQLException {
		String sql =
                "CREATE TABLE IF NOT EXISTS TAGS " + 
					"(TAG_ID integer primary key autoincrement, " +  
					"TAG_NAME text " +
					");";
		DbUtil.execUpdateSql(connection,sql);
		initAutoIncrementSequence(connection, "TAGS");
	}
	private void createUsers(Connection connection) throws SQLException {
		createUsersTable(connection);
		AddSysadminUser();
	}

	private void createUsersTable(Connection connection) throws SQLException {
		String sql =
                "CREATE TABLE IF NOT EXISTS USERS " + 
					"(USER_NO text primary key, " +
					"USER_NAME text, " +
					"PASSWORD text, " + 
					"E_MAIL text, " + 
					"MUST_CHANGE_PASSWORD integer, " +
					"ACCESS_LEVEL text " +
					");";
		DbUtil.execUpdateSql(connection,sql);
	}

	private void initAutoIncrementSequence(Connection connection, String name) throws SQLException {
		String sql = "insert into sqlite_sequence (name, seq) values (?, 0)";
		DbUtil.execUpdateSql(connection,sql, name);
	}

	private void AddVoice(String code, String name) {
		Voice v = new Voice(code, name);
		entityManager.persist(v);
	}
	private void AddSysadminUser() {
		User user = new User();
		user.setNo("sysadmin");
		user.setName("System Admin Big Boss");
		user.setPassword(PasswordUtil.getPasswordMd5Hash("sommer"));
		user.seteMail("jarle.jacobsen@gmail.com");
		user.setAccessLevel(AccessLevel.SYSADMIN);
		user.setMustChangePassword(true);
		entityManager.persist(user);
	}


}
