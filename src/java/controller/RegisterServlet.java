package controller;

import strategy.StrategyContext;
import util.ValidationException;
import util.ErrorPageResolver;
import util.RegistrationRequestBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;
import util.UserIdExtractor;
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

        try {
            Object dto = RegistrationRequestBuilder.buildDTO(userType, req);
            logger.info("Processing registration for userType: " + userType);

            strategyContext.executeStrategy(userType, dto);

            String userId = UserIdExtractor.extractId(userType, dto);
            logger.info("Registration successful for userId: " + userId);

            resp.sendRedirect(OTP_VERIFICATION_PAGE + "?" + USER_ID + "=" + userId + "&" + USER_TYPE + "=" + userType);

        } catch (ValidationException ve) {
            logger.warning("Validation failed: " + ve.getMessage());
            req.setAttribute(ERROR, ve.getMessage());
            String targetPage = ErrorPageResolver.resolve(userType);
            req.getRequestDispatcher(targetPage).forward(req, resp);
        } catch (Exception e) {

            req.setAttribute("error", e.getMessage());

String targetPage = switch (userType) {
    case "customer" -> "customerRegister.jsp";
    case "admin" -> "adminRegister.jsp";
    case "staff" -> "staffRegister.jsp";
    default -> "index.jsp"; 
};

req.getRequestDispatcher(targetPage).forward(req, resp);


            logger.severe("Unexpected error: " + e.getMessage());
            req.setAttribute(ERROR, GENERIC_ERROR);
           
            req.getRequestDispatcher(targetPage).forward(req, resp);

        }
    }
}
