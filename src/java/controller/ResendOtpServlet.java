package controller;

import dao.*;
import db.DBConnection;
import model.*;
import service.common.OtpSendService;
import service.common.OtpSendServiceImpl;
import util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

public class ResendOtpServlet extends HttpServlet {

    private CustomerDAO customerDAO;
    private AdminDAO adminDAO;
    private StaffDAO staffDAO;
    private OtpSendService otpSendService;

    @Override
public void init() throws ServletException {
    try {
        Connection connection = DBConnection.getInstance().getConnection();
        customerDAO = new CustomerDAOimpl(connection);
        adminDAO = new AminDAOImpl(connection);  // make sure spelling is correct
        staffDAO = new StaffDAOImpl(connection);
        OtpTokenDAO otpTokenDAO = new OtpTokenDAOImpl(connection);
        OtpSender emailService = EmailServiceFactory.createOtpEmailService();

        this.otpSendService = new OtpSendServiceImpl(otpTokenDAO, emailService);  // <-- fix here

    } catch (Exception e) {
        throw new ServletException("Failed to initialize ResendOtpServlet", e);
    }
}


  @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String userId = req.getParameter("userId");
    String userType = req.getParameter("userType");

    try {
        String email = null;

        switch (userType) {
            case "customer" -> {
                Customer customer = customerDAO.findById(userId);
                if (customer != null) email = customer.getEmail();
            }
            case "admin" -> {
                Admin admin = adminDAO.findById(userId);
                if (admin != null) email = admin.getEmail();
            }
            case "staff" -> {
                Staff staff = staffDAO.findById(userId);
                if (staff != null) email = staff.getEmail();
            }
            default -> throw new IllegalArgumentException("Invalid user type");
        }

        if (email != null) {
            otpSendService.sendOtp(userId, userType, email);
            req.setAttribute("success", "A new OTP has been sent to your email.");
        } else {
            req.setAttribute("error", "Failed to find user for OTP resend.");
        }

    } catch (Exception e) {
        // Log the full exception (to server logs)
        e.printStackTrace();

        // Show a simple user-friendly message (avoid exposing internal details)
        req.setAttribute("error", "Failed to resend OTP. Please try again later.");
    }

    req.setAttribute("userId", userId);
    req.setAttribute("userType", userType);
    req.getRequestDispatcher("otp-verification.jsp").forward(req, resp);
}
}