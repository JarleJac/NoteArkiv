package jacJarSoft.noteArkiv.dao;

import jacJarSoft.noteArkiv.model.Note;

public class NoteDao {
	public Note getNote(long noteId) {
		Note note = new Note();
		note.setNoteId(noteId);
		note.setTitle("Whatever");
		note.setDescription("Whatever descr");
		return note;
	}

}
