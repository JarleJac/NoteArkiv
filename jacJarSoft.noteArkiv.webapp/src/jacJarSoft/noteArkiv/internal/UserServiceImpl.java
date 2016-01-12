package jacJarSoft.noteArkiv.internal;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.api.ChangePassword;
import jacJarSoft.noteArkiv.api.LogonInfo;
import jacJarSoft.noteArkiv.dao.UserDao;
import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.service.UserService;
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
		User userLogonInfo = getUserLogonInfo(param);
		User user = userDao.logon(userLogonInfo);
		if (null == user)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		
		if (getCurrentUser().equals(userNo))
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

}
