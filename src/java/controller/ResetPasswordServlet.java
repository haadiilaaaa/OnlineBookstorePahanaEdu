package controller;

import dao.*;
import db.DBConnection;
import model.PasswordResetToken;
import util.*;
import util.contannts.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ResetPasswordServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ResetPasswordServlet.class.getName());

    private PasswordResetTokenDAO tokenDAO;
    private Map<String, PasswordUpdatabale> userPasswordServices;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            tokenDAO = new PasswordResetTokenDAOImpl(connection);

            userPasswordServices = new HashMap<>();
            userPasswordServices.put(RoleConstants.CUSTOMER, new CustomerDAOimpl(connection));
            userPasswordServices.put(RoleConstants.ADMIN, new AminDAOImpl(connection));
            userPasswordServices.put(RoleConstants.STAFF, new StaffDAOImpl(connection));

        } catch (Exception e) {
            throw new ServletException("Failed to initialize ResetPasswordServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String token = req.getParameter(ParameterKeys.TOKEN);

        if (token == null || token.isBlank()) {
            req.setAttribute(AttributeKeys.ERROR, MessageResolver.get("reset.token.missing"));
            req.getRequestDispatcher(PagePaths.RESET_PASSWORD_PAGE).forward(req, resp);
            return;
        }

        try {
            PasswordResetToken resetToken = tokenDAO.findByToken(token);

            if (resetToken == null || resetToken.isUsed()) {
                req.setAttribute(AttributeKeys.ERROR, MessageResolver.get("reset.token.invalid"));
            } else if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                Duration expiredDuration = Duration.between(resetToken.getExpiresAt(), LocalDateTime.now());
                long minutesAgo = expiredDuration.toMinutes();
                req.setAttribute(AttributeKeys.ERROR, MessageResolver.get("reset.token.expired", minutesAgo));
            } else {
                req.setAttribute(ParameterKeys.TOKEN, token);
            }

        } catch (Exception e) {
            logger.severe("Token validation error: " + e.getMessage());
            req.setAttribute(AttributeKeys.ERROR, MessageResolver.get("reset.internal_error"));
        }

        req.getRequestDispatcher(PagePaths.RESET_PASSWORD_PAGE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String token = req.getParameter(ParameterKeys.TOKEN);
        String password = req.getParameter(ParameterKeys.NEW_PASSWORD);
        String confirmPassword = req.getParameter(ParameterKeys.CONFIRM_PASSWORD);

        if (token == null || token.isBlank()) {
            forwardWithError(req, resp, MessageResolver.get("reset.token.missing"));
            return;
        }

        // Rate limiting to prevent abuse
        String rateLimitKey = "reset:" + token;
        if (!ResetRateLimiter.tryAcquire(rateLimitKey)) {
            long waitSec = ResetRateLimiter.getRetryAfter(rateLimitKey) / 1000;
            forwardWithError(req, resp, MessageResolver.get("reset.rate_limited", waitSec));
            return;
        }

        if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            req.setAttribute(ParameterKeys.TOKEN, token);
            forwardWithError(req, resp, MessageResolver.get("reset.password.mismatch"));
            return;
        }

        try {
            PasswordResetToken resetToken = tokenDAO.findByToken(token);

            if (resetToken == null || resetToken.isUsed()
                    || resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                forwardWithError(req, resp, MessageResolver.get("reset.token.invalid"));
                return;
            }

            String userType = resetToken.getUserType().toLowerCase();
            String userId = resetToken.getUserId();
            PasswordUpdatabale dao = userPasswordServices.get(userType);

            if (dao == null) {
                forwardWithError(req, resp, MessageResolver.get("reset.unsupported_user", userType));
                return;
            }

            // Securely hash password and update
            String hashed = PasswordHasher.hashPassword(password);
            dao.updatePassword(userId, hashed);
            tokenDAO.markAsUsed(token);
            ResetRateLimiter.reset(rateLimitKey);

            logger.info("Password reset successful → userId=" + userId + ", userType=" + userType);

            HttpSession session = req.getSession();
            session.setAttribute(SessionKeys.SUCCESS_MESSAGE, MessageResolver.get("reset.success"));
            resp.sendRedirect(PagePaths.LOGIN_PAGE);

        } catch (Exception e) {
            logger.severe("Password reset failed: " + e.getMessage());
            req.setAttribute(ParameterKeys.TOKEN, token);
            forwardWithError(req, resp, MessageResolver.get("reset.internal_error"));
        }
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp, String errorMsg)
            throws ServletException, IOException {
        req.setAttribute(AttributeKeys.ERROR, errorMsg);
        req.getRequestDispatcher(PagePaths.RESET_PASSWORD_PAGE).forward(req, resp);
    }
}
