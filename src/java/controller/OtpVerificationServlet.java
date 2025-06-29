package controller;

import dao.*;
import db.DBConnection;
import service.common.OtpVerificationService;
import service.common.OtpVerificationServiceImpl;
// ✅ CORRECT for Tomcat 9
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class OtpVerificationServlet extends HttpServlet {

    private OtpVerificationService otpService;

    @Override
    public void init() throws ServletException {
        try {
            // ✅ Get one shared connection
            Connection connection = DBConnection.getInstance().getConnection();

            // ✅ Pass it to each DAO
            OtpTokenDAO otpTokenDAO = new OtpTokenDAOImpl(connection);
            CustomerDAO customerDAO = new CustomerDAOimpl(connection);
            AdminDAO adminDAO = new AminDAOImpl(connection);
            StaffDAO staffDAO = new StaffDAOImpl(connection);

            // ✅ Pass DAOs to the service
            otpService = new OtpVerificationServiceImpl(otpTokenDAO, customerDAO, adminDAO, staffDAO);
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
        System.out.println("Verifying OTP: " + enteredOtp + " for userId=" + userId + " and userType=" + userType);
        boolean isVerified = otpService.verifyOtp(userId, userType, enteredOtp);

        if (isVerified) {
           resp.sendRedirect("login.jsp?success=Registration+successful!+You+can+now+log+in.");

        } else {
            req.setAttribute("error", "Invalid or expired OTP.");
            req.setAttribute("userId", userId);
            req.setAttribute("userType", userType);
            req.getRequestDispatcher("otp-verification.jsp").forward(req, resp);
        }

    } catch (Exception e) {
        req.setAttribute("error", "Verification failed: " + e.getMessage());
        req.setAttribute("userId", userId);
        req.setAttribute("userType", userType);
        req.getRequestDispatcher("otp-verification.jsp").forward(req, resp);
    }
}

}
