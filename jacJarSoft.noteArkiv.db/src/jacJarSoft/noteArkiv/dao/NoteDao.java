package jacJarSoft.noteArkiv.dao;

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

}
