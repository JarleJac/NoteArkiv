
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
import jacJarSoft.noteArkiv.webapi.SetPwInfo;
import jacJarSoft.noteArkiv.webapi.UserInfoReturn;
import jacJarSoft.noteArkiv.webapi.UserTokenReturn;
import jacJarSoft.util.PasswordUtil;
import jacJarSoft.util.Auth.AuthTokenInfo;
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
		if (user.getAccessLevel() == AccessLevel.DISABLED)
			throw new ValidationErrorException("Bruker har blitt sperret");

		ForgotPwMailSender forgotPwMailSender = new ForgotPwMailSender(user, getAppSettings(), getFreemarkerConfig(), getNotearkivBaseUrl());
		forgotPwMailSender.sendMail();
		return Response.ok(new UserInfoReturn(user)).build();
	}

	@Override
	public Response getUserFromToken(String token) {
		UserTokenReturn resp = new UserTokenReturn();
		AuthTokenInfo tokenInfo = null;
		try {
			tokenInfo = AuthTokenUtil.getTokenInfo(token);
			resp.setStatusOk(true);
		} catch (Exception e) {
			resp.setErrorMsg("Ugyldig eller utløpt autorisasjonstoken!");
		}

		if (resp.isStatusOk())
		{
			resp.setUser(userDao.getUser(tokenInfo.getUser()));
			resp.setToken(AuthTokenUtil.createToken(tokenInfo.getUser(), "setPw", 120));
		}
		return Response.ok(resp).build();
	}

	@Override
	public Response setPw(SetPwInfo setPwInfo) {
		AuthTokenInfo tokenInfo = null;
		try {
			tokenInfo = AuthTokenUtil.getTokenInfo(setPwInfo.getToken());
		} catch (Exception e) {
			throw new ValidationErrorException("Ugyldig eller utløpt autorisasjonstoken!");
		}
		
		final String userNo = tokenInfo.getUser();
		
		return runWithTransaction((em, p) -> {
			User user = userDao.getUser(userNo);
			
			user.setMustChangePassword(false);
			user.setPassword(PasswordUtil.getPasswordMd5Hash(setPwInfo.getNewpassword()));
			
			user = userDao.updateUser(user);
		
			return Response.ok(new UserInfoReturn(user)).build();
		}, null);
	}
}
