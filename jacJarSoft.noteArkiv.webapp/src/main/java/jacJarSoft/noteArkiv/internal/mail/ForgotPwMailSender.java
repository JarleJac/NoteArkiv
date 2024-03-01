package jacJarSoft.noteArkiv.internal.mail;

import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jacJarSoft.noteArkiv.internal.AppInfo;
import jacJarSoft.noteArkiv.internal.NoteArkivSettings;
import jacJarSoft.noteArkiv.model.User;

public class ForgotPwMailSender {
    private static Logger logger = Logger.getLogger(ForgotPwMailSender.class.getName());

	private User user;
	private AppInfo appInfo;

	private Configuration configuration;

	public ForgotPwMailSender(User user, AppInfo appInfo, Configuration configuration) {
		this.user = user;
		this.appInfo = appInfo;
		this.configuration = configuration;
	}

	public void sendMail() {
		logger.info("forgot email called for user " + user.getNo());
		String htmlContent = builMailHtml();
		
		String subject = "Nullstille passordet for din bruker til " + appInfo.getAppSettings().getApplicationTitle();
		String recipientTO = "jarle.jacobsen@gmail.com";
		
		MailSender mailSender = MailSenderFactory.getMailSender();
		
		mailSender.sendMail(subject, htmlContent, recipientTO);
		
	}

	private String builMailHtml() {
		String htmlContent;
		try {
			Map<String, Object> data = new HashMap<>();
			data.put("user", user);
			data.put("appInfo", appInfo);
			data.put("resetPwUrl", "http:/something.com");
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
