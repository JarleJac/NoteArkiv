package jacJarSoft.noteArkiv.webapi;

import jacJarSoft.noteArkiv.api.UserGravatarInfo;
import jacJarSoft.noteArkiv.model.User;

public class UserInfoReturn {

	private User user;
	private UserGravatarInfo gravatar;

	public UserInfoReturn(User user) {
		setUser(user);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		gravatar = new UserGravatarInfo(user);
	}

	public UserGravatarInfo getGravatar() {
		return gravatar;
	}

	public void setGravatar(UserGravatarInfo gravatar) {
		this.gravatar = gravatar;
	}

}