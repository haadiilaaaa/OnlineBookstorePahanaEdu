package util.redirect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StaffRedirectStrategy implements LoginRedirectStrategy {
    @Override
    public void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("StaffDashboardServlet");
    }
}
