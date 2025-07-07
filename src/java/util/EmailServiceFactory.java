package util;

import java.util.Properties;

public class EmailServiceFactory {

    private static Properties loadMailProperties() {
        String smtpHost = System.getenv("SMTP_HOST");
        String smtpPort = System.getenv("SMTP_PORT");
        String fromEmail = System.getenv("EMAIL_USER");
        String password = System.getenv("EMAIL_PASSWORD");

        if (smtpHost == null || smtpHost.isBlank()) {
            throw new IllegalStateException("SMTP_HOST environment variable is not set");
        }
        if (smtpPort == null || smtpPort.isBlank()) {
            throw new IllegalStateException("SMTP_PORT environment variable is not set");
        }
        if (fromEmail == null || fromEmail.isBlank()) {
            throw new IllegalStateException("EMAIL_USER environment variable is not set");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("EMAIL_PASSWORD environment variable is not set");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.debug", "true");

        return props;
    }

    // ✅ For OTP-only emails
    public static OtpSender createOtpEmailService() {
        Properties props = loadMailProperties();
        String fromEmail = System.getenv("EMAIL_USER");
        String password = System.getenv("EMAIL_PASSWORD");

        return new OtpEmailSenderImpl(fromEmail, password, props);
    }

    // ✅ For general-purpose email (e.g., reset link)
    public static EmailSender createGeneralEmailService() {
        Properties props = loadMailProperties();
        String fromEmail = System.getenv("EMAIL_USER");
        String password = System.getenv("EMAIL_PASSWORD");

        return new GeneralEmailSenderImpl(fromEmail, password, props);
    }
}
