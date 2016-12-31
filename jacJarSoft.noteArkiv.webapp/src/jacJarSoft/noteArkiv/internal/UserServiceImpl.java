package jacJarSoft.noteArkiv.internal;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.dao.UserDao;
import jacJarSoft.noteArkiv.model.AccessLevel;
import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.service.UserService;
import jacJarSoft.noteArkiv.webapi.ChangePassword;
import jacJarSoft.noteArkiv.webapi.LogonInfo;
import jacJarSoft.noteArkiv.webapi.UserInfoReturn;
import jacJarSoft.util.PasswordUtil;
import jacJarSoft.util.StringUtils;

public class UserServiceImpl extends BaseService implements UserService {
	@Autowired
	private UserDao userDao;

	public static User getUserLogonInfo(LogonInfo logonInfo) {
		User userLogonInfo = new User();
		userLogonInfo.setNo(logonInfo.getUser());
		userLogonInfo.setPassword(PasswordUtil.getPasswordMd5Hash(logonInfo.getPassword()));
		return userLogonInfo;
	}

	@Override
	public Response changePassword(String userNo, ChangePassword param) {
		if (!param.getUser().equals(userNo))
			return Response.status(Response.Status.UNAUTHORIZED).build();

		return runWithTransaction(this::internalChangePassword, param);

	}
	private Response internalChangePassword(EntityManager em, ChangePassword param) {
		User user = null;
		if (getCurrentUser().equals(param.getUser())) {
			User userLogonInfo = getUserLogonInfo(param);
			user = userDao.logon(userLogonInfo);
		}
		else {
			User userLogonInfo = getUserLogonInfo(new LogonInfo(getCurrentUser(), param.getPassword()));
			User logonReturn = userDao.logon(userLogonInfo);
			if (logonReturn != null)
				user = userDao.getUser(param.getUser());
		}
		if (null == user)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		
		if (getCurrentUser().equals(param.getUser()))
		{
			user.setMustChangePassword(false);
		}
		else
		{
			User loggedOnUser = getVerifiedUser(getCurrentUser());

			if (loggedOnUser.getAccessLevel().getLevel() < AccessLevel.ADMIN.getLevel())
				throw new ValidationErrorException("Din bruker har ikke lov til å endre passord for andre brukere.");
				
			user.setMustChangePassword(true);
		}
		
		user.setPassword(PasswordUtil.getPasswordMd5Hash(param.getNewpassword()));
		user = userDao.updateUser(user);
		return Response.ok(user).build();
		
	}

	@Override
	public Response getUser(String userNo) {
		User user = getVerifiedUser(userNo);
		UserInfoReturn ret = new UserInfoReturn(user);
		return Response.ok(ret).build();
	}

	private User getVerifiedUser(String userNo) {
		User user = userDao.getUser(userNo);
		if (user == null)
			throw new ValidationErrorException("Ukjent bruker " + userNo);
		return user;
	}

	@Override
	public Response addUser(User user) {
		validateUser(user, true);
		User readUser = userDao.getUser(user.getNo());
		if (readUser != null)
			throw new ValidationErrorException("Bruker id "+ user.getNo() +" finnes fra før!");

		user.setPassword(PasswordUtil.getPasswordMd5Hash(user.getPassword()));
		user.setMustChangePassword(true);
		return runWithTransaction((ec, p)-> {
			return Response.ok(userDao.addUser(user)).build();
		}, null);
	}
	private void validateUser(User user, boolean isNew) {
		if (StringUtils.isEmpty(user.getNo()))
			throw new ValidationErrorException("Bruker id kan ikke være blank");
		if (StringUtils.isEmpty(user.getName()))
			throw new ValidationErrorException("Bruker navn kan ikke være blank");
		if (isNew && StringUtils.isEmpty(user.getPassword()))
			throw new ValidationErrorException("passord kan ikke være blank");
	}

	@Override
	public Response updateUser(User user) {
		validateUser(user, false);
		return runWithTransaction((ec, p)-> {
			User readUser = userDao.getUser(user.getNo());
			readUser.setAccessLevel(user.getAccessLevel());
			readUser.seteMail(user.geteMail());
			readUser.setName(user.getName());
			return Response.ok(userDao.updateUser(readUser)).build();
		}, null);
	}
	@Override
	public Response getUsers() {
		return Response.ok(userDao.getUsers()).build();
	}

	@Override
	public Response deleteUser(String userNo) {
		User user = getVerifiedUser(userNo);
		return runWithTransaction((ec, p)-> {
			userDao.deleteUser(user);
			return Response.ok().build();
		}, null);
	}
}
