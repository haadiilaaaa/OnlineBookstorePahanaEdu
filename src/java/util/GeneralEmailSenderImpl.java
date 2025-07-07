package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class GeneralEmailSenderImpl implements EmailSender {

    private final String fromEmail;
    private final String password;
    private final Properties mailProperties;

    public GeneralEmailSenderImpl(String fromEmail, String password, Properties mailProperties) {
        this.fromEmail = fromEmail;
        this.password = password;
        this.mailProperties = mailProperties;
    }

    @Override
    public void sendEmail(String toEmail, String subject, String body) throws Exception {
        Session session = Session.getInstance(mailProperties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
    @Override
public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String filename) throws Exception {
    Session session = Session.getInstance(mailProperties, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fromEmail, password);
        }
    });

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(fromEmail));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
    message.setSubject(subject);

    // Body part
    MimeBodyPart messageBodyPart = new MimeBodyPart();
    messageBodyPart.setContent(body, "text/html");

    // Attachment part
    MimeBodyPart attachmentPart = new MimeBodyPart();
    attachmentPart.attachFile(new java.io.File(filename));
    attachmentPart.setFileName(filename);
    attachmentPart.setContent(attachment, "application/pdf");

    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(messageBodyPart);
    multipart.addBodyPart(attachmentPart);

    message.setContent(multipart);
    Transport.send(message);
}

}
