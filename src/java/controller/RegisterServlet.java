package controller;

import service.common.RegistrationFacadeService;
import util.ValidationException;
import util.ErrorPageResolver;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.contannts.PagePaths.*;
import static util.contannts.ErrorMessages.*;
import static util.contannts.ParameterKeys.*;
import static util.contannts.AttributeKeys.*;

public class RegisterServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());
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
            logger.warning("User type is missing or empty.");
            req.setAttribute(ERROR, "User type is required.");
            req.getRequestDispatcher(LOGIN_PAGE).forward(req, resp);
            return;
        }

        try {
            String userId = registrationFacadeService.register(userType, req);
            logger.info("Registration successful for userId: " + userId);

            resp.sendRedirect(OTP_VERIFICATION_PAGE + "?" + USER_ID + "=" + userId + "&" + USER_TYPE + "=" + userType);

        } catch (ValidationException ve) {
            logger.warning("Validation failed: " + ve.getMessage());
            req.setAttribute(ERROR, ve.getMessage());
            req.getRequestDispatcher(ErrorPageResolver.resolve(userType)).forward(req, resp);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during registration", e);
            req.setAttribute(ERROR, GENERIC_ERROR);
            req.getRequestDispatcher(ErrorPageResolver.resolve(userType)).forward(req, resp);
        }
    }
}
