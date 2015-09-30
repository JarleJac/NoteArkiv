package jacJarSoft.noteArkiv.db;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jacJarSoft.noteArkiv.base.NoteArkivAppInfo;
import jacJarSoft.util.DbUtil;
import jacJarSoft.util.ExceptionUtil;
import jacJarSoft.util.Version;

public class NoteArkivDatabase {

	private static Logger logger = Logger.getLogger(NoteArkivDatabase.class.getName());
	private File dbFileName;

	public NoteArkivDatabase(String dbFileName) {
		File f = new File(dbFileName);
		this.dbFileName = f.getAbsoluteFile();
	}

	public void verifyAndUpgradeDb() {
		if (!dbFileName.exists())
			createEmptyDb();
		
		upgradeDb();
		
	}
	private void upgradeDb() {
		try {
			try (Connection connection = getConnection()) {
				String versionStr =
					DbUtil.execQuery(DbUtil.createStatement(connection, "Select version_no from system_info"), (rs)-> {
						if (!rs.next())
							throw new SQLException("No rows in system info");
						return rs.getString(1);
					});
				Version version = new Version(versionStr);
				if (version.isEarlier(NoteArkivAppInfo.getVersion()))
				{
					if (version.getMajor() == 0 && version.getMinor() == 0) {
						createNewDatabase();
					}
					else if (version.getMajor() < 2)
						upgradeFromVersion1();
				}
					
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error upgrading db", e);
			throw ExceptionUtil.getRuntimeException(e);
		}
		
		
	}

	private void upgradeFromVersion1() {
		// TODO Auto-generated method stub createNewDatabase
		
	}

	private void createNewDatabase() {
		// TODO Auto-generated method stub createNewDatabase
		
	}

	private void createEmptyDb() {
		try {
			Files.createDirectories(dbFileName.getParentFile().toPath());
			try (Connection connection = getConnection()) {
				createVersionTable(connection);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error creating db", e);
			throw ExceptionUtil.getRuntimeException(e);
		}
	}


	private void createVersionTable(Connection connection) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS SYSTEM_INFO (" +
					 "VERSION_NO text " +
					 ")";
		DbUtil.execUpdateSql(connection,sql);
		DbUtil.execUpdateSql(connection,"INSERT INTO SYSTEM_INFO (VERSION_NO) VALUES(?)","0.0.0.0");
	}

	private Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFileName);
		return connection;
	}

}
