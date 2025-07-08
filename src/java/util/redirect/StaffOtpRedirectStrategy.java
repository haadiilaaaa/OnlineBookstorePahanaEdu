package util.redirect;

import util.contannts.PagePaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StaffOtpRedirectStrategy implements OtpRedirectStrategy {
    @Override
    public void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Redirect to login page with success message
        resp.sendRedirect(req.getContextPath() + "/login.jsp?success=Verification+successful!+You+can+now+login.");
    }
}
