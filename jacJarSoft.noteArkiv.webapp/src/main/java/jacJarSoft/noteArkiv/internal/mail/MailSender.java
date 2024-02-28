package jacJarSoft.noteArkiv.internal.mail;

public interface MailSender {
	
	void sendMail(String subject, String htmlContent, String recipientTO);
}
