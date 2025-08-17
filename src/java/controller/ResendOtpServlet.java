package controller;

import dao.*;
import db.DBConnection;
import model.enums.UserType;
import service.common.*;
import util.*;
import util.contannts.*;
import model.User;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;  

public class ResendOtpServlet extends HttpServlet {

    private UserService userService;
    private OtpSendService otpSendService;
    private OtpRateLimiter otpRateLimiter;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, GenericUserDAO<? extends User>> daoMap = new HashMap<>();
            daoMap.put(UserType.CUSTOMER.getValue(), new CustomerDAOimpl(connection));
            daoMap.put(UserType.ADMIN.getValue(), new AminDAOImpl(connection));
            daoMap.put(UserType.STAFF.getValue(), new StaffDAOImpl(connection));
            daoMap.put(UserType.DELIVERY.getValue(), new DeliveryPartnerDAOImpl(connection));

            userService = new UserServiceImpl(Collections.unmodifiableMap(daoMap));

            OtpTokenDAO otpTokenDAO = new OtpTokenDAOImpl(connection);
            otpSendService = new OtpSendServiceImpl(otpTokenDAO, EmailServiceFactory.createOtpEmailService());

            otpRateLimiter = GlobalOtpRateLimiter.getInstance();

        } catch (Exception e) {
            System.out.println("Initialization failed: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("ResendOtpServlet init failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userId = req.getParameter(ParameterKeys.USER_ID);
        String userType = req.getParameter(ParameterKeys.USER_TYPE);

        if (userId == null || userId.isBlank() ||
            userType == null || userType.isBlank() ||
            !UserType.isValid(userType)) {

            req.setAttribute(AttributeKeys.ERROR, MessageResolver.get("otp.invalid_request"));
            req.getRequestDispatcher(PagePaths.OTP_VERIFICATION_PAGE).forward(req, resp);
            return;
        }

        String rateLimitKey = userType + ":" + userId;

        if (!otpRateLimiter.tryAcquire(rateLimitKey)) {
            long waitMs = otpRateLimiter.getRetryAfter(rateLimitKey);
            long waitSec = waitMs / 1000;
            req.setAttribute(AttributeKeys.ERROR, MessageResolver.get("otp.too_many_requests", waitSec));
            forwardBack(req, resp, userId, userType);
            return;
        }

        try {
            String email = userService.getEmailByUserIdAndType(userId.trim(), userType.trim());

            if (email != null && !email.isEmpty()) {
                otpSendService.sendOtp(userId, userType, email);
                req.setAttribute(AttributeKeys.SUCCESS, MessageResolver.get("otp.sent_success"));
                System.out.println("OTP resent → userId=" + userId + ", userType=" + userType);
            } else {
                req.setAttribute(AttributeKeys.ERROR, MessageResolver.get("otp.user_not_found"));
            }

        } catch (Exception e) {
            System.out.println("OTP resend error for userId=" + userId + ": " + e.getMessage());
            e.printStackTrace();
            req.setAttribute(AttributeKeys.ERROR, MessageResolver.get("otp.internal_error"));
        }

        forwardBack(req, resp, userId, userType);
    }

    private void forwardBack(HttpServletRequest req, HttpServletResponse resp, String userId, String userType)
            throws ServletException, IOException {
        req.setAttribute(ParameterKeys.USER_ID, userId);
        req.setAttribute(ParameterKeys.USER_TYPE, userType);
        req.getRequestDispatcher(PagePaths.OTP_VERIFICATION_PAGE).forward(req, resp);
    }
}
