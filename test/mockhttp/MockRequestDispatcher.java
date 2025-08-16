package mockhttp;

import javax.servlet.*;
import java.io.IOException;

public class MockRequestDispatcher implements RequestDispatcher {
    private final String path;

    public MockRequestDispatcher(String path) {
        this.path = path;
    }

    @Override
    public void forward(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        if (response instanceof MockHttpServletResponse) {
            ((MockHttpServletResponse) response).setForwardedUrl(path);
        }
    }

    @Override
    public void include(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        // Not needed for your tests
    }
}
