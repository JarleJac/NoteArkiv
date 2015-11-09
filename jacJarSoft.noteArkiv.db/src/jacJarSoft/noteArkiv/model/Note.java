package jacJarSoft.noteArkiv.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="NOTES")
@XmlRootElement(name="Note")
public class Note extends AbstractEntity {
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
	@Column(name="REGISTERED_DATE")
    private Date registeredDate;
	@Column(name="REGISTERED_BY")
    private String registeredBy;
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
	public Date getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	public String getRegisteredBy() {
		return registeredBy;
	}
	public void setRegisteredBy(String registeredBy) {
		this.registeredBy = registeredBy;
	}

}
