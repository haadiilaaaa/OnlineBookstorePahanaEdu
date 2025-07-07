package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class OtpEmailSenderImpl implements OtpSender {

    private final String fromEmail;
    private final String password;
    private final Properties mailProperties;

    public OtpEmailSenderImpl(String fromEmail, String password, Properties mailProperties) {
        this.fromEmail = fromEmail;
        this.password = password;
        this.mailProperties = mailProperties;
    }

    @Override
    public void sendOTP(String toEmail, String otpCode) throws Exception {
        Session session = Session.getInstance(mailProperties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otpCode + "\n\nThis code will expire in 5 minutes.");

        Transport.send(message);
    }
}
