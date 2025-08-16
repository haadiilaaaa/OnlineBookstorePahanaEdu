package controller;

import service.common.RegistrationFacadeService;
import util.ValidationException;
import util.ErrorPageResolver;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

import static util.contannts.PagePaths.*;
import static util.contannts.ErrorMessages.*;
import static util.contannts.ParameterKeys.*;
import static util.contannts.AttributeKeys.*;

public class RegisterServlet extends HttpServlet {

    private RegistrationFacadeService registrationFacadeService;

    @Override
    public void init() throws ServletException {
        registrationFacadeService = (RegistrationFacadeService) getServletContext().getAttribute("RegistrationFacadeService");
        if (registrationFacadeService == null) {
            throw new ServletException("RegistrationFacadeService not found.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userType = req.getParameter(USER_TYPE);

        if (userType == null || userType.isEmpty()) {
            System.out.println("User type is missing or empty.");
            req.setAttribute(ERROR, "User type is required.");
            req.getRequestDispatcher(LOGIN_PAGE).forward(req, resp);
            return;
        }

        // In RegisterServlet's doPost()
try {
    String userId = registrationFacadeService.register(userType, req);
    System.out.println("Registration successful for userId: " + userId);
    System.out.println("SUCCESS: Redirecting for userId=" + userId); // Check test logs
    resp.sendRedirect(OTP_VERIFICATION_PAGE + "?" + USER_ID + "=" + userId + "&" + USER_TYPE + "=" + userType);

} catch (ValidationException ve) {
    System.out.println("Validation failed: " + ve.getMessage());
    
    // Handle specific validation errors
    String errorMessage = ve.getMessage();
    if (errorMessage.contains("already registered")) {
        req.setAttribute(ERROR, "Email already registered");
    } else {
        req.setAttribute(ERROR, errorMessage);
    }
    
    req.getRequestDispatcher(ErrorPageResolver.resolve(userType)).forward(req, resp);

} catch (Exception e) {
    System.out.println("Unexpected error during registration: " + e.getMessage());
    e.printStackTrace();
    req.setAttribute(ERROR, GENERIC_ERROR);
    req.getRequestDispatcher(ErrorPageResolver.resolve(userType)).forward(req, resp);
}
    }
}
