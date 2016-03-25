package jacJarSoft.noteArkiv.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="LIST_NOTES")
@XmlRootElement(name="ListNote")
public class SheetListNote extends AbstractEntity {
	@Id @Column(name="LIST_ID")
	private long listId;
	@Id @Column(name="NOTE_ID")
	private long noteId;

}
