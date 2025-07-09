package filter;

import dto.UserSession;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

public class AdminAuthorizationFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(AdminAuthorizationFilter.class.getName());
    private static final String USER_SESSION_KEY = "user";
    private static final String REQUIRED_ROLE = "admin";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        if (session == null) {
            LOGGER.warning("No session found, redirecting to login.");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UserSession user = (UserSession) session.getAttribute(USER_SESSION_KEY);
        if (user == null) {
            LOGGER.warning("User not logged in, redirecting to login.");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if (!REQUIRED_ROLE.equalsIgnoreCase(user.getUserType())) {
            LOGGER.warning("User " + user.getEmail() + " unauthorized to access admin resource.");
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        // User is authorized, continue the request
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {}
    @Override
    public void destroy() {}
}
