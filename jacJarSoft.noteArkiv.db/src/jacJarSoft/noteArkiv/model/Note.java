package jacJarSoft.noteArkiv.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="note")
public class Note {
	private long noteId;
    private String title;
    private String  composedBy;
    private String  arrangedBy;
    private String description;
	public long getNoteId() {
		return noteId;
	}
	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getComposedBy() {
		return composedBy;
	}
	public void setComposedBy(String composedBy) {
		this.composedBy = composedBy;
	}
	public String getArrangedBy() {
		return arrangedBy;
	}
	public void setArrangedBy(String arrangedBy) {
		this.arrangedBy = arrangedBy;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
