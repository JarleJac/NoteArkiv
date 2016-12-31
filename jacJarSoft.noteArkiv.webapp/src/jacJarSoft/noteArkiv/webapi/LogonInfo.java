package jacJarSoft.noteArkiv.webapi;

public class LogonInfo {
	private String user;
	private String password;
	public LogonInfo() {
		
	}
	public LogonInfo(String user, String pw) {
		this.user = user;
		password = pw;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
