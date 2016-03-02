package jacJarSoft.noteArkiv.internal;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.dao.UserDao;
import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.service.UserService;
import jacJarSoft.noteArkiv.webapi.ChangePassword;
import jacJarSoft.noteArkiv.webapi.LogonInfo;
import jacJarSoft.noteArkiv.webapi.UserInfoReturn;
import jacJarSoft.util.PasswordUtil;

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
		User userLogonInfo = getUserLogonInfo(param);
		User user = userDao.logon(userLogonInfo);
		if (null == user)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		
		if (getCurrentUser().equals(param.getUser()))
		{
			user.setMustChangePassword(false);
		}
		else
		{
			//TODO check that user is admin
			user.setMustChangePassword(true);
		}
		
		user.setPassword(PasswordUtil.getPasswordMd5Hash(param.getNewpassword()));
		user = userDao.updateUser(user);
		return Response.ok(user).build();
		
	}

	@Override
	public Response getUser(String userNo) {
		User user = userDao.getUser(userNo);
		if (user == null)
			throw new ValidationErrorException("Ukjent bruker " + userNo);
		UserInfoReturn ret = new UserInfoReturn(user);
		return Response.ok(ret).build();
	}

	@Override
	public Response addUser(User user) {
		return runWithTransaction((ec, p)-> {
			return Response.ok(userDao.addUser(user)).build();
		}, null);
	}
	@Override
	public Response getUsers() {
		return Response.ok(userDao.getUsers()).build();
	}
}
