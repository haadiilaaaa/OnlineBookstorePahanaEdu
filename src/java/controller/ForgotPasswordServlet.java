// =====================
// ForgotPasswordServlet.java
// =====================
package controller;

import dao.*;
import db.DBConnection;
import model.PasswordResetToken;
import service.common.PasswordResetTokenFactory;
import service.common.UserService;
import service.common.UserServiceImpl;
import util.EmailSender;
import util.EmailServiceFactory;
import util.ForgotPasswordEmailTemplateBuilder;
import util.contannts.AttributeKeys;
import util.contannts.PagePaths;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Logger;
import dto.*;
public class ForgotPasswordServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ForgotPasswordServlet.class.getName());

    private UserService userService;
    private PasswordResetTokenDAO tokenDAO;
    private EmailSender emailSender;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

           Map<String, GenericUserDAO> daoMap = new HashMap<>();
daoMap.put("customer", new CustomerDAOimpl(connection));
daoMap.put("admin", new AminDAOImpl(connection));
daoMap.put("staff", new StaffDAOImpl(connection));

userService = new UserServiceImpl(daoMap);

            tokenDAO = new PasswordResetTokenDAOImpl(connection);
            emailSender = EmailServiceFactory.createGeneralEmailService();

        } catch (Exception e) {
            throw new ServletException("Failed to initialize ForgotPasswordServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        if (email == null || email.isBlank()) {
            req.setAttribute(AttributeKeys.ERROR, "Please enter your email.");
            req.getRequestDispatcher(PagePaths.FORGOT_PASSWORD_PAGE).forward(req, resp);
            return;
        }

        try {
            var userOpt = userService.findUserIdAndTypeByEmail(email.trim());

            if (userOpt.isEmpty()) {
                req.setAttribute(AttributeKeys.ERROR, "No account found with that email.");
                req.getRequestDispatcher(PagePaths.FORGOT_PASSWORD_PAGE).forward(req, resp);
                return;
            }

            var user = userOpt.get();
            PasswordResetToken resetToken = PasswordResetTokenFactory.createToken(user.getUserId(), user.getUserType());
            tokenDAO.save(resetToken);

            String resetLink = req.getScheme() + "://" + req.getServerName() + ":" +
                    req.getServerPort() + req.getContextPath() + "/resetPassword.jsp?token=" + resetToken.getToken();

            String subject = "Password Reset Request";
            String message = ForgotPasswordEmailTemplateBuilder.buildResetPasswordMessage(resetLink);

            emailSender.sendEmail(email, subject, message);

            req.setAttribute(AttributeKeys.SUCCESS, "Password reset link has been sent to your email.");
        } catch (Exception e) {
            logger.severe("Error processing password reset: " + e.getMessage());
            req.setAttribute(AttributeKeys.ERROR, "Error processing password reset request.");
        }

        req.getRequestDispatcher(PagePaths.FORGOT_PASSWORD_PAGE).forward(req, resp);
    }
}
