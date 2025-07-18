package util.redirect;

import util.contannts.PagePaths;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeliveryPartnerOTPRedirectStrategy implements OtpRedirectStrategy {

    @Override
    public void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Redirect to the waiting for admin approval page
        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath + PagePaths.DELIVERY_PARTNER_WAITING_APPROVAL_PAGE);
    }
}
