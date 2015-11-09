package jacJarSoft.noteArkiv.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="VOICES")
@XmlRootElement(name="Voice")
public class Voice  extends AbstractEntity {
	public Voice() {}
	public Voice(String code, String name){
		this.code = code;
		this.name = name;
	}
	@Id
	@GeneratedValue(generator="sqlite_person")
	@TableGenerator(name="sqlite_person", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="VOICES",
	    initialValue=1, allocationSize=1)	
	@Column(name="VOICE_ID")
	private int id;
	@Column(name="VOICE_CODE")
	private String code;
	@Column(name="VOICE_NAME")
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
