package controller;

import strategy.StrategyContext;
import util.ValidationException;
import util.ErrorPageResolver;
import util.RegistrationRequestBuilder;
import util.UserIdExtractor;

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
    private StrategyContext strategyContext;

    @Override
    public void init() throws ServletException {
        strategyContext = (StrategyContext) getServletContext().getAttribute("StrategyContext");
        if (strategyContext == null) {
            throw new ServletException("StrategyContext not found.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userType = req.getParameter(USER_TYPE);

        // Null or empty userType validation
        if (userType == null || userType.isEmpty()) {
            logger.warning("User type is missing or empty.");
            req.setAttribute(ERROR, "User type is required.");
            req.getRequestDispatcher(LOGIN_PAGE).forward(req, resp);
            return;
        }

        try {
            Object dto = RegistrationRequestBuilder.buildDTO(userType, req);
            logger.info("Processing registration for userType: " + userType);

            strategyContext.executeStrategy(userType, dto);

            String userId = UserIdExtractor.extractId(userType, dto);
            logger.info("Registration successful for userId: " + userId);

            // Redirect to OTP verification page
            resp.sendRedirect(OTP_VERIFICATION_PAGE + "?" + USER_ID + "=" + userId + "&" + USER_TYPE + "=" + userType);

        } catch (ValidationException ve) {
            logger.warning("Validation failed: " + ve.getMessage());
            req.setAttribute(ERROR, ve.getMessage());

            // Use ErrorPageResolver for error page path
            String targetPage = ErrorPageResolver.resolve(userType);
            req.getRequestDispatcher(targetPage).forward(req, resp);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during registration", e);
            req.setAttribute(ERROR, GENERIC_ERROR);

            // fallback error page
            String targetPage = ErrorPageResolver.resolve(userType);
            req.getRequestDispatcher(targetPage).forward(req, resp);
        }
    }
}
