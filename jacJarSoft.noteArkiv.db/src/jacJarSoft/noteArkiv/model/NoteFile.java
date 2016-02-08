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
public class NoteFile extends AbstractEntity {
	@Id
	@GeneratedValue(generator="sqlite_note_files")
	@TableGenerator(name="sqlite_note_files", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="NOTE_FILES",
	    initialValue=1, allocationSize=1)	
	@Column(name="NOTE_FILE_ID")
	private long noteFileId;
	@Column(name="NOTE_ID")
	private long noteId;
	@Column(name="FILE_NAME")
    private String fileName;
	@Column(name="DESCRIPTION")
    private String description;
	@Column(name="FILE_SIZE")
	private long fileSize;
	@Column(name="REGISTERED_DATE")
    private Date registeredDate;
	@Column(name="REGISTERED_BY")
    private String registeredBy;

}
