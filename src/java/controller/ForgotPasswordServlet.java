package controller;

import dao.*;
import db.DBConnection;
import model.PasswordResetToken;
import util.EmailSender;
import util.EmailServiceFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.UUID;

public class ForgotPasswordServlet extends HttpServlet {

    private CustomerDAO customerDAO;
    private AdminDAO adminDAO;
    private StaffDAO staffDAO;
    private PasswordResetTokenDAO tokenDAO;
    private EmailSender emailSender;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            customerDAO = new CustomerDAOimpl(connection);
            adminDAO = new AminDAOImpl(connection);
            staffDAO = new StaffDAOImpl(connection);
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

        try {
            if (email == null || email.isBlank()) {
                req.setAttribute("error", "Please enter your email.");
                req.getRequestDispatcher("forgotPassword.jsp").forward(req, resp);
                return;
            }

            // Try find user by email in all user DAOs
            String userId = null;
            String userType = null;

            var customer = customerDAO.findByEmail(email);
            if (customer != null) {
                userId = customer.getId(); // or getId() depending on your model
                userType = "customer";
            }

            var admin = adminDAO.findByEmail(email);
            if (admin != null) {
                userId = admin.getId();
                userType = "admin";
            }

            var staff = staffDAO.findByEmail(email);
            if (staff != null) {
                userId = staff.getId();
                userType = "staff";
            }

            if (userId == null) {
                req.setAttribute("error", "No account found with that email.");
                req.getRequestDispatcher("forgotPassword.jsp").forward(req, resp);
                return;
            }

            // Generate token and expiry (e.g., 30 minutes from now)
            String token = UUID.randomUUID().toString();
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setId(UUID.randomUUID().toString());
            resetToken.setUserId(userId);
            resetToken.setUserType(userType);
            resetToken.setToken(token);
            resetToken.setExpiresAt(expiresAt);
            resetToken.setUsed(false);
            resetToken.setCreatedAt(LocalDateTime.now());

            // Save token in DB
            tokenDAO.save(resetToken);

            // Create reset link
            String resetLink = req.getScheme() + "://" + req.getServerName() + ":" +
                    req.getServerPort() + req.getContextPath() + "/resetPassword.jsp?token=" + token;

            // Send email
            String subject = "Password Reset Request";
            String message = "Hi,\n\nClick the following link to reset your password:\n" + resetLink +
                    "\n\nThis link will expire in 30 minutes.";

            emailSender.sendEmail(email, subject, message);

            req.setAttribute("success", "Password reset link has been sent to your email.");
            req.getRequestDispatcher("forgotPassword.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error processing password reset request.");
            req.getRequestDispatcher("forgotPassword.jsp").forward(req, resp);
        }
    }
}
