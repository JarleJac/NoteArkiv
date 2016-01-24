package jacJarSoft.noteArkiv.webapi;

import jacJarSoft.noteArkiv.model.User;

public class LogonInfoReturn {
	private User user;
	private String authToken;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
}
