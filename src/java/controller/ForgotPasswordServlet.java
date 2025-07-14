package controller;

import dao.*;
import db.DBConnection;
import service.common.ForgotPasswordService;
import service.common.ForgotPasswordServiceFactory;
import util.EmailSender;
import util.EmailServiceFactory;
import util.contannts.AttributeKeys;
import util.contannts.PagePaths;
import model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class ForgotPasswordServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ForgotPasswordServlet.class.getName());

    private ForgotPasswordService forgotPasswordService;

     @Override
    public void init() throws ServletException {
        forgotPasswordService = (ForgotPasswordService) getServletContext().getAttribute("ForgotPasswordService");
        if (forgotPasswordService == null) {
            throw new ServletException("ForgotPasswordService not found in ServletContext.");
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

    // Build base URL for reset link
    String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();

    try {
        boolean success = forgotPasswordService.processForgotPassword(email, baseUrl);

        if (!success) {
            req.setAttribute(AttributeKeys.ERROR, "No account found with that email.");
        } else {
            req.setAttribute(AttributeKeys.SUCCESS, "Password reset link has been sent to your email.");
        }

    } catch (Exception e) {
        logger.severe("Error processing password reset: " + e.getMessage());
        req.setAttribute(AttributeKeys.ERROR, "Error processing password reset request.");
    }

    req.getRequestDispatcher(PagePaths.FORGOT_PASSWORD_PAGE).forward(req, resp);
}

}
