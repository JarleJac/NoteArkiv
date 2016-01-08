package jacJarSoft.noteArkiv.dao;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.model.User;

@Component
public class UserDao extends AbstractDao {

	public User Logon(User logonInfo) {
		User user = getEntityManager().find(User.class, logonInfo.getNo());
		if (!user.getPassword().equals(logonInfo.getPassword()))
			return null;
		return user;
	}

}
