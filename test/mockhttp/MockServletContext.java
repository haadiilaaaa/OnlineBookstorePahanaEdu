package mockhttp;

import javax.servlet.*;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import javax.servlet.jsp.JspConfigDescriptor;

public class MockServletContext implements ServletContext {

    private final Map<String, Object> attributes = new HashMap<>();
    private final Map<String, String> initParams = new HashMap<>();

    @Override
    public Enumeration<String> getServletNames() {
        return Collections.emptyEnumeration();
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    @Override
    public void declareRoles(String[] roleNames) {
        // no-op
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        return null;
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        return Collections.emptySet();
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        return Collections.emptyEnumeration();
    }

    @Override
    public void addListener(EventListener listener) {
        // no-op
    }

    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {
        // no-op
    }

    @Override
    public void addListener(String className) {
        // no-op
    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int sessionTimeout) {
        // no-op
    }

    @Override
    public String getInitParameter(String name) {
        return initParams.get(name);
    }

    @Override
    public String getServerInfo() {
        return "MockServletContext";
    }

    @Override
    public String getMimeType(String file) {
        return null;
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        initParams.put(name, value);
        return true;
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setRequestCharacterEncoding(String encoding) {
        // no-op
    }

    @Override
    public String getResponseCharacterEncoding() {
        return null;
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return Collections.emptySet();
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return Collections.emptySet();
    }

    @Override
    public String getRequestCharacterEncoding() {
        return null;
    }

    @Override
    public void setResponseCharacterEncoding(String encoding) {
        // no-op
    }

    @Override
    @Deprecated
    public Servlet getServlet(String name) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, String className) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String servletName, String jspFile) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        return null;
    }

    @Override
    public String getServletContextName() {
        return "MockServletContext";
    }

    @Override
    public String getVirtualServerName() {
        return "MockServer";
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        return null;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        // no-op
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return Collections.emptyMap();
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParams.keySet());
    }
    
    @Override
public javax.servlet.descriptor.JspConfigDescriptor getJspConfigDescriptor() {
    return null;
}


    

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return Collections.emptyMap();
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return null;
    }

    @Override
    public int getEffectiveMajorVersion() {
        return 4;
    }

    @Override
    public int getEffectiveMinorVersion() {
        return 0;
    }

    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        return null;
    }

    @Override
    public void log(String message, Throwable throwable) {
        System.out.println(message);
        throwable.printStackTrace();
    }

    @Override
    public void log(String msg) {
        System.out.println(msg);
    }

    @Override
    @Deprecated
    public void log(Exception exception, String msg) {
        System.out.println(msg);
        exception.printStackTrace();
    }

    @Override
    public ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        return null;
    }

    @Override
    public URL getResource(String path) {
        return null;
    }

    @Override
    public ServletContext getContext(String uripath) {
        return this;
    }

    @Override
    public void setAttribute(String name, Object object) {
        attributes.put(name, object);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public int getMajorVersion() {
        return 4;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }
}
