package jacJarSoft.noteArkiv.dao;

import java.util.Date;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.model.Note;

@Component
public class NoteDao extends AbstractDao {
	
	public Note getNote(long noteId) {
		Note note = getEntityManager().find(Note.class, noteId);
		return note;
	}

	public Note insertNote(Note sheet) {
		sheet.setRegisteredDate(new Date());
		sheet.setRegisteredBy(getCurrentUser());
		getEntityManager().persist(sheet);
		return sheet;
	}

}
