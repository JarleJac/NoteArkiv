package jacJarSoft.noteArkiv.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.model.NoteFile;
import jacJarSoft.noteArkiv.model.NoteFileData;

@Component
public class SheetFileDao extends AbstractDao {

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
	public NoteFileData insertNoteFileData(NoteFileData sheetFileData) {
		getEntityManager().persist(sheetFileData);
		return sheetFileData;
	}
	public NoteFileData getFileData(long fileId) {
		return getEntityManager().find(NoteFileData.class, fileId);
	}
	public NoteFile getNoteFile(long fileId) {
		return getEntityManager().find(NoteFile.class, fileId);
	}

}
