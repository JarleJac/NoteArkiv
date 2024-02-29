package jacJarSoft.noteArkiv.internal.mail;

import jacJarSoft.noteArkiv.model.User;

public class ForgotPwMailSender {

	private User user;

	public ForgotPwMailSender(User user) {
		this.user = user;
	}

	public void sendMail() {
		String htmlContent = builMailHtml();
		
		String subject = "aaddff æøåÆØÅ";
		String recipientTO = "jarle.jacobsen@gmail.com";
		
		MailSender mailSender = MailSenderFactory.getMailSender();
		
		mailSender.sendMail(subject, htmlContent, recipientTO);
		// TODO Auto-generated method stub
		
	}

	private String builMailHtml() {
		String htmlContent = """
				<html><body>
					Hei ${userName}<br />
					Din bruker er ${user}<br />
					Denne skulle egentlig vært sendt til ${userMail}<br />æøåÆØÅ
				</body></html>
				""";
		htmlContent = htmlContent.replace("${userName}", user.getName());
		htmlContent = htmlContent.replace("${user}", user.getNo());
		htmlContent = htmlContent.replace("${userMail}", user.geteMail());
		return htmlContent;
	}

}
