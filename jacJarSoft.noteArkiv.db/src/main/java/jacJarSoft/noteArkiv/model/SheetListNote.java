package jacJarSoft.noteArkiv.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="LIST_NOTES")
@XmlRootElement(name="ListNote")
@IdClass(value=SheetListNote.MyIdClass.class)
public class SheetListNote extends AbstractEntity {
	public SheetListNote() {}
	public SheetListNote(long listId, long noteId) {
		this.setListId(listId);
		this.setNoteId(noteId);
	}
	public long getListId() {
		return listId;
	}
	public void setListId(long listId) {
		this.listId = listId;
	}
	public long getNoteId() {
		return noteId;
	}
	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}
	@Id @Column(name="LIST_ID")
	private long listId;
	@Id @Column(name="NOTE_ID")
	private long noteId;

	public static class MyIdClass {
		public long listId;
		public long noteId;
	};
}
