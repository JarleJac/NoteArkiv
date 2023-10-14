package jacJarSoft.noteArkiv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="TAGS")
@XmlRootElement(name="Tag")
public class Tag extends AbstractEntity {
	@Id
	@GeneratedValue(generator="sqlite_tag")
	@TableGenerator(name="sqlite_tag", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="TAGS",
	    initialValue=1, allocationSize=1)	
	@Column(name="TAG_ID")
	private int id;
	@Column(name="TAG_NAME")
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
