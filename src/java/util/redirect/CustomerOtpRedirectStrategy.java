package util.redirect;

import util.contannts.PagePaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerOtpRedirectStrategy implements OtpRedirectStrategy {
    @Override
    public void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(PagePaths.CUSTOMER_DASHBOARD);
    }
}
