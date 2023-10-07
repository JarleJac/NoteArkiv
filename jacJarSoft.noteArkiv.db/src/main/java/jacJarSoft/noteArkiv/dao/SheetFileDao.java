package jacJarSoft.noteArkiv.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.db.util.SheetFileUtil;
import jacJarSoft.noteArkiv.model.NoteFile;
import jacJarSoft.util.ExceptionUtil;
import jacJarSoft.util.FileUtil;

@Component
public class SheetFileDao extends AbstractDao {
	private static Logger logger = Logger.getLogger(SheetFileDao.class.getName());

	public List<NoteFile> getSheetFiles(long sheetId) {
		@SuppressWarnings("unchecked")
		List<NoteFile> sheetFiles = (List<NoteFile>) getEntityManager()
				.createNativeQuery("select * from NOTE_FILES where note_Id = ? order by NOTE_FILE_ID",NoteFile.class)
				.setParameter(1, sheetId)
				.getResultList();
		return sheetFiles;
	}
	public NoteFile insertNoteFile(NoteFile sheetFile) {
		sheetFile.setRegisteredDate(new Date());
		sheetFile.setRegisteredBy(getCurrentUser());
		getEntityManager().persist(sheetFile);
		return sheetFile;
	}
//	@Deprecated
//	public NoteFileData insertNoteFileData(NoteFileData sheetFileData) {
//		getEntityManager().persist(sheetFileData);
//		return sheetFileData;
//	}
//	@Deprecated
//	public NoteFileData getFileData(long fileId) {
//		return getEntityManager().find(NoteFileData.class, fileId);
//	}
	public NoteFile getNoteFile(long fileId) {
		return getEntityManager().find(NoteFile.class, fileId);
	}
	public void deleteFile(NoteFile noteFile) {
		deleteFileData(noteFile);
		getEntityManager().remove(noteFile);
	}
	private void deleteFileData(NoteFile sheetFile) {
		Path filePath = getFilePath(sheetFile);
		try {
			FileUtil.deleteFile(filePath, SheetFileUtil.getFileName(sheetFile));
		} catch (IOException e) { 
			//ignore this, just log some info
			logger.warning("Unable to delete file " + filePath.toString());
		}
	}
	//	@Deprecated
//	private void deleteFileData(long fileId) {
//		DbUtil.runWithConnection(getEntityManager(), (con)-> {
//			try(Statement s = con.createStatement()) {
//				s.execute("delete from NOTE_FILES_DATA where NOTE_FILE_ID = " + fileId);
//			}
//			return null;
//		});
//	}
	public NoteFile updateFile(NoteFile file) {
		NoteFile oldFile = getNoteFile(file.getFileId());
		if (!oldFile.getName().equals(file.getName()))
			renameFileOnDisk(oldFile, file);
		getEntityManager().merge(file);
		return file;
	}
	private void renameFileOnDisk(NoteFile oldFile, NoteFile file) {
		try {
			FileUtil.renameFile(getFilePath(file), SheetFileUtil.getFileName(oldFile),SheetFileUtil.getFileName(file));
		} catch (IOException e) {
			throw ExceptionUtil.getRuntimeException(e);
		}
		
	}
	public void insertSheetFileData(NoteFile sheetFile, byte[] bytes) throws FileNotFoundException, IOException {
		FileUtil.writeBytesToFile(getFilePath(sheetFile), SheetFileUtil.getFileName(sheetFile), bytes);
	}
	private Path getFilePath(NoteFile sheetFile) {
		String sheetIdPath = String.format("%1$05d", sheetFile.getNoteId());
		return Paths.get(getFilesDirectory(), sheetIdPath);
	}
	public byte[] getSheetFileData(NoteFile sheetFile) throws IOException {
		return FileUtil.readBytesFromFile(getFilePath(sheetFile), SheetFileUtil.getFileName(sheetFile), (int)sheetFile.getFileSize());
	}
	public File getSheetFileDataAsFile(NoteFile sheetFile) {
		return Paths.get(getFilePath(sheetFile).toString(), SheetFileUtil.getFileName(sheetFile)).toFile();
	}

}
