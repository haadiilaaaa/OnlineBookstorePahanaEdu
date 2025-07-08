package util.redirect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface OtpRedirectStrategy {
    void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
