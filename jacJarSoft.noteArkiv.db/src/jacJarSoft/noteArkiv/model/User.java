package jacJarSoft.noteArkiv.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

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
	@Column(name="IS_SYSADMIN")
	private boolean isSysadmin;
	@Column(name="IS_ADMIN")
	private boolean isAdmin;
	@Column(name="IS_AUTHOR")
	private boolean isAuthor;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
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
	public boolean isSysadmin() {
		return isSysadmin;
	}
	public void setSysadmin(boolean isSysadmin) {
		this.isSysadmin = isSysadmin;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public boolean isAuthor() {
		return isAuthor;
	}
	public void setAuthor(boolean isAuthor) {
		this.isAuthor = isAuthor;
	}
	@PostLoad
	private void postLoad() {
		//password = "**hidden**";
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
}
