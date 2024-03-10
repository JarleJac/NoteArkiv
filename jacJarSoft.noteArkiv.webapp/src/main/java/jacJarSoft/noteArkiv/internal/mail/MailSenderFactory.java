package jacJarSoft.noteArkiv.internal.mail;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class MailSenderFactory {

	private static MailSenderFactory instance;
	private static Object instanceLock = new Object();
	private static Properties mailprops;
	private static String propertyPrefix;

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
		MailSenderFactory factory = new MailSenderFactory();

		return factory;
	}

	public static MailSender getMailSender() {
		return getInstance().getMailSenderInternal();
	}
	
	public static void configure(Properties mailprops, String propertyPrefix)	{
		if (MailSenderFactory.mailprops != null)
			throw new IllegalStateException("Alredy configured");
		
		MailSenderFactory.mailprops = mailprops;
		MailSenderFactory.propertyPrefix = propertyPrefix;
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

				Multipart multipart = new MimeMultipart();

				BodyPart htmlPart = new MimeBodyPart();
				htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");
				multipart.addBodyPart(htmlPart);

				mimeMessage.setContent(multipart);

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

	private String mailUser;
	private String mailpw;

	private MailSenderFactory() {
		mailUser = System.getProperty(propertyPrefix + "notearkiv.email.user");
		String mailpwEnc = System.getProperty(propertyPrefix + "notearkiv.email.pw");
		if (StringUtils.isEmpty(mailUser))
		{
			mailUser = System.getProperty("notearkiv.email.user");
			mailpwEnc = System.getProperty("notearkiv.email.pw");
		}
		mailpw = new String(Base64.getDecoder().decode(mailpwEnc.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
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
