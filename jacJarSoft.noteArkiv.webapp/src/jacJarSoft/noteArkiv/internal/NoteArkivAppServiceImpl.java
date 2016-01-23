
package jacJarSoft.noteArkiv.internal;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.api.LogonInfo;
import jacJarSoft.noteArkiv.api.LogonInfoReturn;
import jacJarSoft.noteArkiv.base.NoteArkivAppInfo;
import jacJarSoft.noteArkiv.dao.UserDao;
import jacJarSoft.noteArkiv.model.AppInfo;
import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.service.NoteArkivAppService;
import jacJarSoft.util.Auth.AuthTokenUtil;

public class NoteArkivAppServiceImpl extends BaseService implements NoteArkivAppService {
	@Autowired
	private UserDao userDao;

	@Override
	public Response getSystemInfo() {
		AppInfo info = new AppInfo();
		return Response.ok(info).build();
	}

	@Override
	public Response logon(LogonInfo logonInfo) {
		User userLogonInfo = UserServiceImpl.getUserLogonInfo(logonInfo);
		
		User user = userDao.logon(userLogonInfo);
		if (null == user)
			throw new ValidationErrorException("Uggyldig bruker og/eller pasord");

		LogonInfoReturn lir = new LogonInfoReturn();
		lir.setUser(user);
		lir.setAuthToken(AuthTokenUtil.createToken(user.getNo(), "NoteArkiv " + NoteArkivAppInfo.getVersion().toVersionString(true)));
		return Response.ok(lir).build();
	}


}
