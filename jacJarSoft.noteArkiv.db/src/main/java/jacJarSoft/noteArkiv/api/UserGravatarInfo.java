package jacJarSoft.noteArkiv.api;

import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.util.GravatarUtil;
import jacJarSoft.util.StringUtils;

public class UserGravatarInfo {
	private String hash;
	private String dParam = "identicon";
	public UserGravatarInfo(User user) {
		String mail = user.geteMail();
		if (StringUtils.hasValue(mail)) {
			setHash(GravatarUtil.md5Hex(mail));
		}
		else {
			setHash(GravatarUtil.md5Hex("???"));
			dParam = "mm";
		}
	}
	public String getdParam() {
		return dParam;
	}
	public void setdParam(String dParam) {
		this.dParam = dParam;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}

}
