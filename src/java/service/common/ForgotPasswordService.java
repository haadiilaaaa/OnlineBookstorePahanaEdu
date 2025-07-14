package service.common;

import dao.GenericUserDAO;
import dao.PasswordResetTokenDAO;
import model.PasswordResetToken;
import model.User;
import util.EmailSender;
import util.ForgotPasswordEmailTemplateBuilder;
import dto.UserIdTypePair;

import java.util.Map;
import java.util.Optional;

public class ForgotPasswordService {

    private final Map<String, GenericUserDAO<? extends User>> userDAOs;
    private final PasswordResetTokenDAO tokenDAO;
    private final EmailSender emailSender;

    public ForgotPasswordService(Map<String, GenericUserDAO<? extends User>> userDAOs,
                                 PasswordResetTokenDAO tokenDAO,
                                 EmailSender emailSender) {
        this.userDAOs = userDAOs;
        this.tokenDAO = tokenDAO;
        this.emailSender = emailSender;
    }

    public boolean processForgotPassword(String email, String baseUrl) throws Exception {
        Optional<UserIdTypePair> userOpt = new UserServiceImpl(userDAOs).findUserIdAndTypeByEmail(email.trim());

        if (userOpt.isEmpty()) {
            return false; // no user found
        }

        UserIdTypePair user = userOpt.get();
        PasswordResetToken resetToken = PasswordResetTokenFactory.createToken(user.getUserId(), user.getUserType());
        tokenDAO.save(resetToken);

        String resetLink = baseUrl + "/resetPassword.jsp?token=" + resetToken.getToken();
        String subject = "Password Reset Request";
        String message = ForgotPasswordEmailTemplateBuilder.buildResetPasswordMessage(resetLink);

        emailSender.sendEmail(email, subject, message);
        return true;
    }
}
