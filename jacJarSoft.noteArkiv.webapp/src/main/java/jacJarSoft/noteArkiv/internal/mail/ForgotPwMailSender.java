package jacJarSoft.noteArkiv.internal.mail;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jacJarSoft.noteArkiv.internal.AppInfo;
import jacJarSoft.noteArkiv.internal.NoteArkivSettings;
import jacJarSoft.noteArkiv.model.User;
import jacJarSoft.util.Auth.AuthTokenUtil;

public class ForgotPwMailSender {
    private static Logger logger = Logger.getLogger(ForgotPwMailSender.class.getName());

	private User user;
	private AppInfo appInfo = new AppInfo();
	private Configuration configuration;
	//private NoteArkivSettings noteArkivSettings;
	private String baseNotearkivUrl;

	public ForgotPwMailSender(User user, NoteArkivSettings noteArkivSettings, Configuration configuration, String baseNotearkivUrl) {
		this.user = user;
		//this.noteArkivSettings = noteArkivSettings;
		this.configuration = configuration;
		this.baseNotearkivUrl = baseNotearkivUrl;

		appInfo.setAppSettings(noteArkivSettings.getJsAppSettings());
	}

	public void sendMail() {
		logger.info("forgot email called for user " + user.getNo());
		String htmlContent = builMailHtml(buildUrl());
		
		String subject = "Angi nytt passord for din bruker til " + appInfo.getAppSettings().getApplicationTitle();
		String recipientTO = user.geteMail();
		
		MailSender mailSender = MailSenderFactory.getMailSender();
		
		mailSender.sendMail(subject, htmlContent, recipientTO);
		
	}

	private String buildUrl() {
		String tokenString = buildToken();
		String resetPwUrl = baseNotearkivUrl + "setpw?spts=" + tokenString;
		return resetPwUrl;
	}

	private String buildToken() {
		String token = AuthTokenUtil.createToken(user.getNo(), "forgotpw", 15);
		try {
			return URLEncoder.encode(token, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Exeption URLEncoding token", e);
			throw new RuntimeException(e);
		}
	}

	private String builMailHtml(String resetPwUrl) {
		String htmlContent;
		try {
			Map<String, Object> data = new HashMap<>();
			data.put("user", user);
			data.put("appInfo", appInfo);
			data.put("resetPwUrl", resetPwUrl);
			Template template = configuration.getTemplate("forgotPw.html");
			StringWriter writer = new StringWriter();
			template.process(data, writer);
			htmlContent = writer.toString();
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Error processing html template for mail", e);
			throw new RuntimeException();
		}
		return htmlContent;
	}

}
