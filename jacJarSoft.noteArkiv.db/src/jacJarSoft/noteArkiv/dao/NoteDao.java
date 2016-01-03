package jacJarSoft.noteArkiv.dao;

import java.util.Date;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.model.Note;

@Component
public class NoteDao extends AbstractDao {
	
	public Note getNote(long noteId) {
		Note note = new Note();
		note.setNoteId(noteId);
		note.setTitle("Whatever");
		note.setDescription("Whatever descr");
		return note;
	}

	public Note insertNote(EntityManager em, Note sheet) {
		sheet.setRegisteredDate(new Date());
		
		em.persist(sheet);
		return sheet;
	}

}
