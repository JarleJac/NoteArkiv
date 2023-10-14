package jacJarSoft.noteArkiv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.xml.bind.annotation.XmlRootElement;

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
	public NoteFile() {
	}
	protected NoteFile(NoteFile other) {
		this.description = other.description;
		this.fileId = other.fileId;
		this.fileSize = other.fileSize;
		this.name = other.name;
		this.noteId = other.noteId;
	}
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
