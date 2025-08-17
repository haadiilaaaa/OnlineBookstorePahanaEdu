package controller;

import service.common.OtpVerificationService;
import util.contannts.ParameterKeys;
import util.contannts.AttributeKeys;
import util.contannts.PagePaths;
import util.contannts.ErrorMessages;
import util.redirect.OtpRedirectStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

public class OtpVerificationServlet extends HttpServlet {

    private OtpVerificationService otpVerificationService;

    // Map of userType -> redirect strategy
    private Map<String, OtpRedirectStrategy> redirectStrategies;

    @Override
    public void init() throws ServletException {
        otpVerificationService = (OtpVerificationService) getServletContext().getAttribute("OtpVerificationService");
        if (otpVerificationService == null) {
            throw new ServletException("OtpVerificationService not initialized");
        }

        Object redirectStrategiesObj = getServletContext().getAttribute("OtpRedirectStrategies");
        if (redirectStrategiesObj instanceof Map<?, ?>) {
            redirectStrategies = (Map<String, OtpRedirectStrategy>) redirectStrategiesObj;
        } else {
            throw new ServletException("OtpRedirectStrategies map not found or invalid");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userId = req.getParameter(ParameterKeys.USER_ID);
        String userType = req.getParameter(ParameterKeys.USER_TYPE);
        String enteredOtp = req.getParameter(ParameterKeys.OTP_CODE);

        if (userId == null || userId.isEmpty() ||
                userType == null || userType.isEmpty() ||
                enteredOtp == null || enteredOtp.isEmpty()) {

            req.setAttribute(AttributeKeys.ERROR, "All fields are required.");
            req.getRequestDispatcher(PagePaths.OTP_VERIFICATION_PAGE).forward(req, resp);
            return;
        }

        try {
            boolean verified = otpVerificationService.verifyOtp(userId, userType, enteredOtp);

            if (verified) {  
                System.out.println("OTP verified successfully for userId: " + userId);

                // Store success message in session (so it survives redirect)
                HttpSession session = req.getSession();
                session.setAttribute("successMessage", "Verification successful! You can now login.");

                OtpRedirectStrategy strategy = redirectStrategies.get(userType);
                if (strategy != null) {
                    strategy.redirect(req, resp);
                } else {
                    resp.sendRedirect("login.jsp");
                }

            } else {
                System.out.println("Failed OTP verification for userId: " + userId);
                req.setAttribute(AttributeKeys.ERROR, "Invalid or expired OTP.");
                req.getRequestDispatcher(PagePaths.OTP_VERIFICATION_PAGE).forward(req, resp);
            }

        } catch (Exception e) {
            System.out.println("Error verifying OTP: " + e.getMessage());
            req.setAttribute(AttributeKeys.ERROR, ErrorMessages.INTERNAL_ERROR);
            req.getRequestDispatcher(PagePaths.OTP_VERIFICATION_PAGE).forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(PagePaths.OTP_VERIFICATION_PAGE).forward(req, resp);
    }
}
