package mockhttp;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.security.Principal;
import java.util.*;

public class MockHttpServletRequest implements HttpServletRequest {

    private Map<String, Object> attributes = new HashMap<>();
    private Map<String, String[]> parameters = new HashMap<>();
    private HttpSession session;
    private String method = "POST";
    private String characterEncoding = "UTF-8";
     private String scheme = "http";
    private String serverName = "localhost";
    private int serverPort = 8080;
    private String contextPath = "";

    public void setParameter(String name, String value) {
        parameters.put(name, new String[]{value});
    }
    
    public void setServerInfo(String scheme, String serverName, int serverPort, String contextPath) {
        this.scheme = scheme;
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.contextPath = contextPath;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override public String getAuthType() { return null; }
    @Override public Cookie[] getCookies() { return new Cookie[0]; }
    @Override public long getDateHeader(String name) { return 0; }
    @Override public String getHeader(String name) { return null; }
    @Override public Enumeration<String> getHeaders(String name) { return Collections.emptyEnumeration(); }
    @Override public Enumeration<String> getHeaderNames() { return Collections.emptyEnumeration(); }
    @Override public int getIntHeader(String name) { return 0; }
    @Override public String getMethod() { return method; }
    @Override public String getPathInfo() { return null; }
    @Override public String getPathTranslated() { return null; }
    @Override public String getContextPath() { return ""; }
    @Override public String getQueryString() { return null; }
    @Override public String getRemoteUser() { return null; }
    @Override public boolean isUserInRole(String role) { return false; }
    @Override public Principal getUserPrincipal() { return null; }
    @Override public String getRequestedSessionId() { return null; }
    @Override public String getRequestURI() { return null; }
    @Override public StringBuffer getRequestURL() { return null; }
    @Override public String getServletPath() { return null; }
   // Corrected getSession method in MockHttpServletRequest
// Corrected getSession method in MockHttpServletRequest
@Override
public HttpSession getSession(boolean create) {
    // If the session exists and has been invalidated, remove the reference.
    if (session instanceof MockHttpSession && ((MockHttpSession) session).isInvalidated()) {
        session = null;
    }

    if (session == null && create) {
        session = new MockHttpSession();
    }
    return session;
}

@Override
public HttpSession getSession() {
    return getSession(true);
}
    
    @Override public String changeSessionId() { return null; }
    @Override public boolean isRequestedSessionIdValid() { return false; }
    @Override public boolean isRequestedSessionIdFromCookie() { return false; }
    @Override public boolean isRequestedSessionIdFromURL() { return false; }
    @Override public boolean isRequestedSessionIdFromUrl() { return false; }
    @Override public boolean authenticate(HttpServletResponse response) { return false; }
    @Override public void login(String username, String password) {}
    @Override public void logout() {}
    @Override public Collection<Part> getParts() { return Collections.emptyList(); }
    @Override public Part getPart(String name) { return null; }
    @Override public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) { return null; }

    @Override public Object getAttribute(String name) { return attributes.get(name); }
    @Override public Enumeration<String> getAttributeNames() { return Collections.enumeration(attributes.keySet()); }
    @Override public String getCharacterEncoding() { return characterEncoding; }
    @Override public void setCharacterEncoding(String env) { this.characterEncoding = env; }
    @Override public int getContentLength() { return 0; }
    @Override public long getContentLengthLong() { return 0; }
    @Override public String getContentType() { return null; }
    @Override public ServletInputStream getInputStream() { return null; }
    @Override public String getParameter(String name) {
        String[] values = parameters.get(name);
        return values != null && values.length > 0 ? values[0] : null;
    }
    @Override public Enumeration<String> getParameterNames() { return Collections.enumeration(parameters.keySet()); }
    @Override public String[] getParameterValues(String name) { return parameters.get(name); }
    @Override public Map<String, String[]> getParameterMap() { return parameters; }
    @Override public String getProtocol() { return "HTTP/1.1"; }
    @Override public String getScheme() { return "http"; }
    @Override public String getServerName() { return "localhost"; }
    @Override public int getServerPort() { return 8080; }
    @Override public BufferedReader getReader() { return null; }
    @Override public String getRemoteAddr() { return "127.0.0.1"; }
    @Override public String getRemoteHost() { return "localhost"; }
    @Override public void setAttribute(String name, Object o) { attributes.put(name, o); }
    @Override public void removeAttribute(String name) { attributes.remove(name); }
    @Override public Locale getLocale() { return Locale.getDefault(); }
    @Override public Enumeration<Locale> getLocales() { return Collections.enumeration(Collections.singletonList(Locale.getDefault())); }
    @Override public boolean isSecure() { return false; }
  @Override
public RequestDispatcher getRequestDispatcher(String path) {
    return new RequestDispatcher() {
        @Override
        public void forward(ServletRequest req, ServletResponse res) {
            if (res instanceof MockHttpServletResponse) {
                ((MockHttpServletResponse) res).setForwardedUrl(path);
            }
        }
        @Override
        public void include(ServletRequest req, ServletResponse res) {}
    };
}

    @Override public String getRealPath(String path) { return null; }
    @Override public int getRemotePort() { return 0; }
    @Override public String getLocalName() { return "localhost"; }
    @Override public String getLocalAddr() { return "127.0.0.1"; }
    @Override public int getLocalPort() { return 8080; }
    @Override public ServletContext getServletContext() { return new MockServletContext(); }
    @Override public AsyncContext startAsync() { return null; }
    @Override public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) { return null; }
    @Override public boolean isAsyncStarted() { return false; }
    @Override public boolean isAsyncSupported() { return false; }
    @Override public AsyncContext getAsyncContext() { return null; }
    @Override public DispatcherType getDispatcherType() { return DispatcherType.REQUEST; }
    
     public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
    
    
    
}
