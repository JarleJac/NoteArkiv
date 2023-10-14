package jacJarSoft.noteArkiv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="NOTES")
@XmlRootElement(name="Note")
public class Note extends RegisteredAwareEntity {
	@Id
	@GeneratedValue(generator="sqlite_notes")
	@TableGenerator(name="sqlite_notes", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="NOTES",
	    initialValue=1, allocationSize=1)	
	@Column(name="NOTE_ID")
	private long noteId;
	@Column(name="TITLE")
    private String title;
	@Column(name="COMPOSED_BY")
    private String  composedBy;
	@Column(name="ARRANGED_BY")
    private String  arrangedBy;
	@Column(name="TAGS")
    private String  tags;
	@Column(name="VOICES")
    private String  voices;
	@Column(name="DESCRIPTION")
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
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getVoices() {
		return voices;
	}
	public void setVoices(String voices) {
		this.voices = voices;
	}

}
