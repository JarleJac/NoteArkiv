package jacJarSoft.noteArkiv.webapi;

import jacJarSoft.noteArkiv.model.User;

public class LogonInfoReturn extends UserInfoReturn {
	private String authToken;

	public LogonInfoReturn(User user) {
		super(user);
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
}
