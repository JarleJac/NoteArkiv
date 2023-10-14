package jacJarSoft.noteArkiv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.xml.bind.annotation.XmlRootElement;

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
	@GeneratedValue(generator="sqlite_voice")
	@TableGenerator(name="sqlite_voice", table="sqlite_sequence",
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
