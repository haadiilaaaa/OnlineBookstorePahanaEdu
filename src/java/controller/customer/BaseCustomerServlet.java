package controller.customer;

import dto.UserSession;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

import static util.contannts.PagePaths.LOGIN_PAGE;
import static util.contannts.SessionKeys.USER;

public abstract class BaseCustomerServlet extends HttpServlet {

    /**
     * Retrieves the authenticated user from the session.
     * Redirects to the login page if no authenticated user is found.
     * @param req The HttpServletRequest.
     * @param resp The HttpServletResponse.
     * @return The UserSession object if authenticated, otherwise null.
     * @throws IOException
     */
    protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(USER) == null) {
            resp.sendRedirect(LOGIN_PAGE);
            return null;
        }
        return (UserSession) session.getAttribute(USER);
    }
}