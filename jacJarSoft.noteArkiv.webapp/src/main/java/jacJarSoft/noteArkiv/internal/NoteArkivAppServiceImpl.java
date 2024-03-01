
package jacJarSoft.noteArkiv.internal;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.base.NoteArkivAppInfo;
import jacJarSoft.noteArkiv.dao.UserDao;
import jacJarSoft.noteArkiv.internal.mail.ForgotPwMailSender;
import jacJarSoft.noteArkiv.model.AccessLevel;
import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.noteArkiv.service.NoteArkivAppService;
import jacJarSoft.noteArkiv.webapi.LogonInfo;
import jacJarSoft.noteArkiv.webapi.LogonInfoReturn;
import jacJarSoft.noteArkiv.webapi.UserInfoReturn;
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

	@Override
	public Response getHelpFilesInfo() {
		//return Response.ok(new ArrayList<FileInfo>()).build();
		return Response.ok(List.of(getAppSettings().getHelpFiles().getFileInfo())).build();
	}

	@Override
	public Response forgotPw(String userOrEmail) {
		if (StringUtils.isAllEmpty(userOrEmail))
			throw new ValidationErrorException("Ingen bruker eller e-post er angitt.");
			
		User user;
		if (userOrEmail.contains("@"))
			user = userDao.getUserFromEmail(userOrEmail);
		else
			user = userDao.getUser(userOrEmail);
		
		if (user == null)
			throw new ValidationErrorException("Finner ikke bruker med id eller e-post " + userOrEmail);
		AppInfo info = new AppInfo();
		info.setAppSettings(getAppSettings().getJsAppSettings());

		ForgotPwMailSender forgotPwMailSender = new ForgotPwMailSender(user, info, getFreemarkerConfig());
		forgotPwMailSender.sendMail();
		return Response.ok(new UserInfoReturn(user)).build();
	}


}
