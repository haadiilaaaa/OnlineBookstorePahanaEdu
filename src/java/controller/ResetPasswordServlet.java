package controller;

import dao.*;
import db.DBConnection;
import model.PasswordResetToken;
import model.Customer;
import model.Admin;
import model.Staff;
import util.PasswordHasher;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;

public class ResetPasswordServlet extends HttpServlet {

    private PasswordResetTokenDAO resetTokenDAO;
    private CustomerDAO customerDAO;
    private AdminDAO adminDAO;
    private StaffDAO staffDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            resetTokenDAO = new PasswordResetTokenDAOImpl(connection);
            customerDAO = new CustomerDAOimpl(connection);
            adminDAO = new AminDAOImpl(connection);
            staffDAO = new StaffDAOImpl(connection);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize ResetPasswordServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String token = req.getParameter("token");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        try {
            if (token == null || token.isBlank()) {
                req.setAttribute("error", "Missing token.");
                req.getRequestDispatcher("resetPassword.jsp").forward(req, resp);
                return;
            }

            PasswordResetToken resetToken = resetTokenDAO.findByToken(token);

            if (resetToken == null || resetToken.isUsed() ||
                resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                req.setAttribute("error", "This password reset link is invalid or expired.");
                req.getRequestDispatcher("resetPassword.jsp").forward(req, resp);
                return;
            }

            if (!password.equals(confirmPassword)) {
                req.setAttribute("error", "Passwords do not match.");
                req.setAttribute("token", token); // Preserve token in case of error
                req.getRequestDispatcher("resetPassword.jsp").forward(req, resp);
                return;
            }

            String hashedPassword = PasswordHasher.hashPassword(password);

            // Determine user type and update password accordingly
            String userId = resetToken.getUserId();
            String userType = resetToken.getUserType();

            switch (userType) {
                case "customer" -> customerDAO.updatePassword(userId, hashedPassword);
                case "admin" -> adminDAO.updatePassword(userId, hashedPassword);
                case "staff" -> staffDAO.updatePassword(userId, hashedPassword);
                default -> throw new IllegalArgumentException("Unknown user type");
            }

            resetTokenDAO.markAsUsed(token);

            req.setAttribute("success", "Password reset successful. You can now log in.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Something went wrong. Please try again.");
            req.setAttribute("token", token);
            req.getRequestDispatcher("resetPassword.jsp").forward(req, resp);
        }
    }
}
