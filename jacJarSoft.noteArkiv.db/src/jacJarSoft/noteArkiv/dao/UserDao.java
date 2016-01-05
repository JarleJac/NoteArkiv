package jacJarSoft.noteArkiv.dao;

import jacJarSoft.noteArkiv.model.User;

public class UserDao extends AbstractDao {
	public User Logon(User logonInfo) {
		User user = getEntityManager().find(User.class, logonInfo.getNo());
		if (!user.getPassword().equals(logonInfo.getPassword()))
			return null;
		return user;
	}

}
