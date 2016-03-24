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
@Table(name="NOTE_FILES")
@XmlRootElement(name="NoteFile")
public class NoteFile extends RegisteredAwareEntity {
	@Id
	@GeneratedValue(generator="sqlite_note_files")
	@TableGenerator(name="sqlite_note_files", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="NOTE_FILES",
	    initialValue=1, allocationSize=1)	
	@Column(name="NOTE_FILE_ID")
	private long fileId;
	@Column(name="NOTE_ID")
	private long noteId;
	@Column(name="FILE_NAME")
    private String name;
	@Column(name="DESCRIPTION")
    private String description;
	@Column(name="FILE_SIZE")
	private long fileSize;
	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	public long getNoteId() {
		return noteId;
	}
	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

}
