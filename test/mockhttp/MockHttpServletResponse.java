package mockhttp;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class MockHttpServletResponse implements HttpServletResponse {

    private String redirectedUrl;
    private String forwardedUrl;
    private StringWriter writer = new StringWriter();
    private String contentType;
    private String characterEncoding = "UTF-8";
    private ServletContext servletContext;
    

      public String getRedirectedUrl() {
        return redirectedUrl;
    }
    public String getForwardedUrl() { return forwardedUrl; }
    public void setForwardedUrl(String url) { this.forwardedUrl = url; }
    public String getWrittenContent() { return writer.toString(); }

    @Override public void addCookie(Cookie cookie) {}
    @Override public boolean containsHeader(String name) { return false; }
    @Override public String encodeURL(String url) { return url; }
    @Override public String encodeRedirectURL(String url) { return url; }
    @Override public String encodeUrl(String url) { return url; }
    @Override public String encodeRedirectUrl(String url) { return url; }
    @Override public void sendError(int sc, String msg) {}
    @Override public void sendError(int sc) {}
    @Override public void sendRedirect(String location) { this.redirectedUrl = location; }
    @Override public void setDateHeader(String name, long date) {}
    @Override public void addDateHeader(String name, long date) {}
    @Override public void setHeader(String name, String value) {}
    @Override public void addHeader(String name, String value) {}
    @Override public void setIntHeader(String name, int value) {}
    @Override public void addIntHeader(String name, int value) {}
    @Override public void setStatus(int sc) {}
    @Override public void setStatus(int sc, String sm) {}

    @Override public int getStatus() { return 200; }
    @Override public String getHeader(String name) { return null; }
    @Override public Collection<String> getHeaders(String name) { return Collections.emptyList(); }
    @Override public Collection<String> getHeaderNames() { return Collections.emptyList(); }

    @Override public String getCharacterEncoding() { return characterEncoding; }
    @Override public String getContentType() { return contentType; }
    @Override public ServletOutputStream getOutputStream() { return null; }
    @Override public PrintWriter getWriter() { return new PrintWriter(writer); }
    @Override public void setCharacterEncoding(String charset) { this.characterEncoding = charset; }
    @Override public void setContentLength(int len) {}
    @Override public void setContentLengthLong(long len) {}
    @Override public void setContentType(String type) { this.contentType = type; }
    @Override public void setBufferSize(int size) {}
    @Override public int getBufferSize() { return 0; }
    @Override public void flushBuffer() {}
    @Override public void resetBuffer() {}
    @Override public boolean isCommitted() { return false; }
    @Override public void reset() {}
    @Override public void setLocale(Locale loc) {}
    @Override public Locale getLocale() { return Locale.getDefault(); }
    
}
