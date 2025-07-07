package controller;

import dao.*;
import db.DBConnection;
import service.common.OtpVerificationService;
import service.common.OtpVerificationServiceImpl;

import java.util.logging.Level;
import service.common.OtpVerificationServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import java.io.IOException;
import java.sql.Connection;
//servlet to hadle the verification of otp
public class OtpVerificationServlet extends HttpServlet {
 private static final Logger logger = Logger.getLogger(OtpVerificationServlet.class.getName());
    private OtpVerificationService otpService;

    @Override
public void init() throws ServletException {
    try {
        otpService = OtpVerificationServiceFactory.createService();
    } catch (Exception e) {
        throw new ServletException("Failed to initialize OtpVerificationServlet", e);
    }
}

    @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String userId = req.getParameter("userId");
    String userType = req.getParameter("userType");
    String enteredOtp = req.getParameter("otp");

    try {
    logger.info("Verifying OTP: " + enteredOtp + " for userId=" + userId + ", userType=" + userType);
    boolean isVerified = otpService.verifyOtp(userId, userType, enteredOtp);

    if (isVerified) {
        resp.sendRedirect("login.jsp?success=Registration+successful!+You+can+now+log+in.");
    } else {
        // Only handle known verification failure here
        req.setAttribute("error", "Invalid or expired OTP.");
        req.setAttribute("userId", userId);
        req.setAttribute("userType", userType);
        req.getRequestDispatcher("otp-verification.jsp").forward(req, resp);
    }

} catch (Exception e) {
    logger.log(Level.SEVERE, "Unexpected verification error", e);
    // Real exception path
    req.setAttribute("error", "An unexpected error occurred. Please try again later.");
    req.setAttribute("userId", userId);
    req.setAttribute("userType", userType);
    req.getRequestDispatcher("otp-verification.jsp").forward(req, resp);
}

}
}
