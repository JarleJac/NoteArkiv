package jacJarSoft.noteArkiv.internal.mail;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import jacJarSoft.noteArkiv.internal.NoteArkivSettings;
import jacJarSoft.noteArkiv.model.User;

public class ForgotPwMailSender {
    private static Logger logger = Logger.getLogger(ForgotPwMailSender.class.getName());

	private User user;
	private NoteArkivSettings noteArkivSettings;

	public ForgotPwMailSender(User user, NoteArkivSettings noteArkivSettings) {
		this.user = user;
		this.noteArkivSettings = noteArkivSettings;
	}

	public void sendMail() {
		logger.info("forgot email caled for user " + user.getNo());
		String htmlContent = builMailHtml();
		
		String subject = "Nullstille passordet for din bruker til " + noteArkivSettings.getJsAppSettings().getApplicationTitle();
		String recipientTO = "jarle.jacobsen@gmail.com";
		
		MailSender mailSender = MailSenderFactory.getMailSender();
		
		mailSender.sendMail(subject, htmlContent, recipientTO);
		
	}

	private String builMailHtml() {
		getClass().getResourceAsStream("forgotPw.html");
		String htmlContent;
		try {
			htmlContent = Files.readString(Paths.get(getClass().getResource("forgotPw.html").toURI()));
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Error processing html template for mail", e);
			throw new RuntimeException();
		}
		return htmlContent;
	}

}
