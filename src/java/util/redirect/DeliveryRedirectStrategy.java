package util.redirect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeliveryRedirectStrategy implements LoginRedirectStrategy {

    @Override
    public void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Redirect delivery partner to delivery dashboard or any page you want
        resp.sendRedirect("DeliveryPartnerDashboardServlet");
    }
}
