package mockhttp;

import javax.servlet.*;

public class MockServletConfig implements ServletConfig {

    private ServletContext servletContext;

    public MockServletConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public String getServletName() {
        return "MockServlet";
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String name) {
        return null;
    }

    @Override
    public java.util.Enumeration<String> getInitParameterNames() {
        return java.util.Collections.emptyEnumeration();
    }
}
