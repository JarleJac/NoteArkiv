package jacJarSoft.noteArkiv.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.model.NoteFile;

@Component
public class SheetFileDao extends AbstractDao {

	public List<NoteFile> getSheetFiles(long sheetId) {
		@SuppressWarnings("unchecked")
		List<NoteFile> sheetFiles = (List<NoteFile>) getEntityManager()
				.createNativeQuery("select * from NOTE_FILES where note_Id = ? order by NOTE_FILE_ID")
				.setParameter(1, sheetId)
				.getResultList();
		return sheetFiles;
	}

}
