package controller;

import dto.UserSession;
import service.common.LoginService;
import service.common.LoginServiceFactory;
import util.LoginAttemptService;
import util.LoginRequestValidator;
import util.redirect.LoginRedirectStrategy;
import util.redirect.RedirectStrategyRegistry;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import static util.contannts.RoleConstants.*;
import static util.contannts.PagePaths.*;
import static util.contannts.SessionKeys.*;
import static util.contannts.ErrorMessages.*;
import static util.contannts.SecurityConstants.*;
import static util.contannts.ParameterKeys.*;
import static util.contannts.AttributeKeys.*;

public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    private LoginService loginService;
    private LoginAttemptService attemptService;

    @Override
    public void init() throws ServletException {
        loginService = LoginServiceFactory.createLoginService();
        attemptService = new LoginAttemptService(MAX_LOGIN_ATTEMPTS, BLOCK_DURATION_MS);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter(USERNAME);
        String password = req.getParameter(PASSWORD);

        Optional<String> validationError = LoginRequestValidator.validate(username, password);
        if (validationError.isPresent()) {
            req.setAttribute(ERROR, validationError.get());
            req.getRequestDispatcher(LOGIN_PAGE).forward(req, resp);
            return;
        }

        if (attemptService.isBlocked(username)) {
            long remaining = attemptService.getRemainingBlockTime(username) / 1000;
            req.setAttribute(ERROR, TOO_MANY_ATTEMPTS + " Try again in " + remaining + " seconds.");
            req.getRequestDispatcher(LOGIN_PAGE).forward(req, resp);
            return;
        }

        try {
            UserSession userSession = loginService.authenticate(username, password);

            if (userSession != null) {
                attemptService.resetAttempts(username);

                HttpSession oldSession = req.getSession(false);
                if (oldSession != null) oldSession.invalidate();

                HttpSession newSession = req.getSession(true);
                newSession.setAttribute(USER, userSession);
                    newSession.setAttribute(USER_ROLE, userSession.getUserType());


                LoginRedirectStrategy strategy = RedirectStrategyRegistry.getStrategy(userSession.getUserType());
                if (strategy != null) {
                    strategy.redirect(req, resp);
                } else {
                    req.setAttribute(ERROR, UNKNOWN_ROLE);
                    req.getRequestDispatcher(LOGIN_PAGE).forward(req, resp);
                }
            } else {
                attemptService.recordFailure(username);
                req.setAttribute(ERROR, INVALID_CREDENTIALS);
                req.getRequestDispatcher(LOGIN_PAGE).forward(req, resp);
            }

        } catch (Exception e) {
            logger.severe("Login error for user " + username + ": " + e.getMessage());
            req.setAttribute(ERROR, INTERNAL_ERROR);
            req.getRequestDispatcher(LOGIN_PAGE).forward(req, resp);
        }
    }
}
