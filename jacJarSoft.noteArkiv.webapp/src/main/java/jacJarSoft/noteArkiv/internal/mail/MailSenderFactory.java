package jacJarSoft.noteArkiv.internal.mail;

import java.util.Date;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailSenderFactory {

	private static MailSenderFactory instance;
	private static Object instanceLock = new Object();

	private static MailSenderFactory getInstance() {
		if (instance == null) {
			synchronized (instanceLock) {
				if (instance == null)
					instance = createInstance();
			}
		}
		return instance;
	}

	private static MailSenderFactory createInstance() {
		// TODO Initialze the factory from settings

		Properties mailprops = new Properties();
		mailprops.put("mail.smtp.host", "smtp.gmail.com");
		mailprops.put("mail.smtp.port", "587");
		mailprops.put("mail.smtp.auth", "true");
		mailprops.put("mail.smtp.starttls.enable", "true");

		MailSenderFactory factory = new MailSenderFactory(mailprops);

		return factory;
	}

	public static MailSender getMailSender() {
		return getInstance().getMailSenderInternal();
	}

	private static class MailSenderImpl implements MailSender {
		private Session session;
		private String from;

		private MailSenderImpl(Session session, String from) {
			this.session = session;
			this.from = from;
		}

		@Override
		public void sendMail(String subject, String htmlContent, String recipientTO) {

			try {
				MimeMessage mimeMessage = buildMimeMessage();
				InternetAddress[] address = { new InternetAddress(recipientTO) };
				mimeMessage.setRecipients(Message.RecipientType.TO, address);
				mimeMessage.setSubject(subject);

				mimeMessage.setContent(htmlContent, "text/html");

				Transport.send(mimeMessage);

			} catch (MessagingException mex) {
				mex.printStackTrace();
				Exception ex = null;
				if ((ex = mex.getNextException()) != null) {
					ex.printStackTrace();
				}
				throw new RuntimeException(mex);
			}
		}

		private MimeMessage buildMimeMessage() {
			MimeMessage msg = new MimeMessage(session);
			try {
				msg.setFrom(new InternetAddress(from));
				msg.setSentDate(new Date());
			} catch (MessagingException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			return msg;
		}
	}

	private String mailUser = "noreply.notearkiv.tjollingsang@gmail.com";
	private String mailpw = "rqyi rdjq ctpq vqmn";
	private Properties mailprops;

	private MailSenderFactory(Properties mailprops) {
		this.mailprops = mailprops;
	}

	private MailSender getMailSenderInternal() {
		return new MailSenderImpl(buildSession(), mailUser);
	}

	private Session buildSession() {
		Authenticator authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailUser, mailpw);
			}
		};

		Session session = Session.getInstance(mailprops, authenticator);
		session.setDebug(false);
		return session;
	}
}
