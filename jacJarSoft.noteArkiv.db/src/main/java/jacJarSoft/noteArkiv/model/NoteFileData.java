package jacJarSoft.noteArkiv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="NOTE_FILES_DATA")
@XmlRootElement(name="NoteFileData")
public class NoteFileData extends AbstractEntity {
	@Id
	@Column(name="NOTE_FILE_ID")
	private long fileId;
	@Lob
	@Column(name="FILE_DATA" )
	private byte[] data;
	
	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}

}
