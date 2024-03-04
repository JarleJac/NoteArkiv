package jacJarSoft.noteArkiv.webapi;

import jacJarSoft.noteArkiv.model.User;

public class UserTokenReturn {
	private boolean statusOk;
	private User user;
	private String errorMsg;

	public boolean isStatusOk() {
		return statusOk;
	}

	public void setStatusOk(boolean statusOk) {
		this.statusOk = statusOk;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
