package jacJarSoft.noteArkiv.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="USERS")
@XmlRootElement(name="User")
public class User extends AbstractEntity{
	@Id
	@Column(name="USER_NO")
	private String no;
	@Column(name="USER_NAME")
	private String name;
	@Column(name="PASSWORD")
	private String password;
	@Column(name="E_MAIL")
	private String eMail;
	@Column(name="MUST_CHANGE_PASSWORD")
	private boolean mustChangePassword;
	@Column(name="ACCESS_LEVEL")
	@Enumerated(EnumType.STRING)
	private AccessLevel accessLevel;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no.toLowerCase();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isMustChangePassword() {
		return mustChangePassword;
	}
	public void setMustChangePassword(boolean mustChangePassword) {
		this.mustChangePassword = mustChangePassword;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public AccessLevel getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}
}
