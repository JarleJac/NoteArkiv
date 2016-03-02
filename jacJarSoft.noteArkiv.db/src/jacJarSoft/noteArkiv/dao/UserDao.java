package jacJarSoft.noteArkiv.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.model.User;

@Component
public class UserDao extends AbstractDao {

	public User logon(User logonInfo) {
		User user = getEntityManager().find(User.class, logonInfo.getNo());
		if (!user.getPassword().equals(logonInfo.getPassword()))
			return null;
		return user;
	}

	public User updateUser(User user) {
		getEntityManager().merge(user);
		return user;
	}
	public User addUser(User user) {
		getEntityManager().persist(user);
		return user;
	}
	public User getUser(String userNo) {
		
		User user = getEntityManager().find(User.class, userNo);
		return user;
	}
	public List<User> getUsers() {
		
		List<User> users = getEntityManager().createQuery("select u from User u", User.class).getResultList();
		return users;
	}

}
