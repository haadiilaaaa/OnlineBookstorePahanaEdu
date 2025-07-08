
package util;

public class ForgotPasswordEmailTemplateBuilder {
    public static String buildResetPasswordMessage(String resetLink) {
        return "Hi,\n\nClick the following link to reset your password:\n" + resetLink +
               "\n\nThis link will expire in 30 minutes.\n\nRegards,\nBookstore Team";
    }
    
}
