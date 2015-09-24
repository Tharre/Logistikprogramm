package Logistik;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * Baut eine Verbindung mit dem email-Server auf Kann überprüfen, ob der User
 * mit diesem Passwort existiert Kann zum Senden von emails verwendet werden
 */

public class IMAP {
	static String protocol = "pop3";
	static String host = "mail.htl-hl.ac.at";
	static String mbox = "INBOX";
	static String url = null;
	static int port = 110;

	public IMAP() {

	}

	public static boolean login(String user, String password) {
		try {

			Properties props = System.getProperties();
			Session session = Session.getInstance(props, null);
			Store store = session.getStore(protocol);
			store.connect(host, port, user, password);
			store.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void sendMail(String sender, String[] empf, String[] bcc,
			String betreff, String text, File attachment) {
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			Session session = Session.getInstance(props, null);
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress(sender));
			InternetAddress[] address = new InternetAddress[empf.length];
			for (int i = 0; i < empf.length; i++) {
				address[i] = new InternetAddress(empf[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(betreff);
			// If the desired charset is known, you can use
			// setText(text, charset)
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(text);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			if (bcc != null) {
				InternetAddress[] bccs = new InternetAddress[bcc.length];
				for (int i = 0; i < bcc.length; i++) {
					bccs[i] = new InternetAddress(bcc[i]);
				}
				msg.setRecipients(Message.RecipientType.BCC, bccs);
			}
			if (attachment != null) {
				messageBodyPart = new MimeBodyPart();
				// Part two is attachment
				DataSource source = new FileDataSource(attachment
						.getAbsolutePath());
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(attachment.getAbsolutePath());
				messageBodyPart.setFileName("Bestellung.pdf");
				multipart.addBodyPart(messageBodyPart);
			}
			// Put parts in message
			msg.setContent(multipart);
			msg.setSentDate(new Date());

			Transport.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}