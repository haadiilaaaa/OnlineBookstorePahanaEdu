package util;

public interface EmailSender {
   
    void sendEmail(String toEmail, String subject, String body) throws Exception;
    void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String filename) throws Exception;

}
