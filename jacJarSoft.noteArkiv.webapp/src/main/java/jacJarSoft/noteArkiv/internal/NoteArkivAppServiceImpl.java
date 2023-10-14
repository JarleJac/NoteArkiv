
package jacJarSoft.noteArkiv.internal;

import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.base.NoteArkivAppInfo;
import jacJarSoft.noteArkiv.dao.UserDao;
import jacJarSoft.noteArkiv.model.AccessLevel;
import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.service.NoteArkivAppService;
import jacJarSoft.noteArkiv.webapi.LogonInfo;
import jacJarSoft.noteArkiv.webapi.LogonInfoReturn;
import jacJarSoft.util.Auth.AuthTokenUtil;
import jakarta.ws.rs.core.Response;

public class NoteArkivAppServiceImpl extends BaseService implements NoteArkivAppService {
	@Autowired
	private UserDao userDao;

	@Override
	public Response getSystemInfo() {
		AppInfo info = new AppInfo();
		info.setAppSettings(getAppSettings().getJsAppSettings());
		return Response.ok(info).build();
	}

	@Override
	public Response logon(LogonInfo logonInfo) {
		User userLogonInfo = UserServiceImpl.getUserLogonInfo(logonInfo);
		
		User user = userDao.logon(userLogonInfo);
		if (null == user)
			throw new ValidationErrorException("Uggyldig bruker og/eller passord");
		if (user.getAccessLevel() == AccessLevel.DISABLED)
			throw new ValidationErrorException("Bruker har blitt sperret");

		LogonInfoReturn lir = new LogonInfoReturn(user);
		lir.setAuthToken(AuthTokenUtil.createToken(user.getNo(), "NoteArkiv " + NoteArkivAppInfo.getVersion().toVersionString(true)));
		return Response.ok(lir).build();
	}


}
