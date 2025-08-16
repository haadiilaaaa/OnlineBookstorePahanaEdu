package mockhttp;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import java.util.*;

public class MockHttpSession implements HttpSession {

    private String id = UUID.randomUUID().toString();
    private Map<String, Object> attributes = new HashMap<>();
    private long creationTime = System.currentTimeMillis();
    private long lastAccessedTime = creationTime;
    private int maxInactiveInterval; // in seconds
    private boolean invalidated = false;
    private ServletContext servletContext;

    public MockHttpSession() {
        this.servletContext = new MockServletContext();
    }

    public MockHttpSession(ServletContext context) {
        this.servletContext = context;
    }

    @Override
    public long getCreationTime() {
        checkValid();
        return creationTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        checkValid();
        return lastAccessedTime;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public Object getAttribute(String name) {
        checkValid();
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        checkValid();
        return Collections.enumeration(attributes.keySet());
    }

    @Override
    public void setAttribute(String name, Object value) {
        checkValid();
        attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        checkValid();
        attributes.remove(name);
    }

    @Override
    public void invalidate() {
        invalidated = true;
        attributes.clear();
    }

    @Override
    public boolean isNew() {
        return false; // always false for mock
    }

    private void checkValid() {
        if (invalidated) {
            throw new IllegalStateException("Session has been invalidated");
        }
    }
    // Inside MockHttpSession class
public boolean isInvalidated() {
    return this.invalidated;
}
    
    

    // Unused methods in your test environment
    @Override public javax.servlet.http.HttpSessionContext getSessionContext() { return null; }
    @Override public Object getValue(String name) { return getAttribute(name); }
    @Override public String[] getValueNames() { return attributes.keySet().toArray(new String[0]); }
    @Override public void putValue(String name, Object value) { setAttribute(name, value); }
    @Override public void removeValue(String name) { removeAttribute(name); }
}
