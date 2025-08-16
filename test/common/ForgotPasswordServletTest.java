package common;

import controller.ForgotPasswordServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.common.ForgotPasswordService;
import util.contannts.AttributeKeys;
import util.contannts.PagePaths;
import mockhttp.*;
import javax.servlet.ServletException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ForgotPasswordServletTest {

    private ForgotPasswordServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletContext servletContext;
    private MockForgotPasswordService forgotPasswordService;

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @Before
    public void setUp() throws Exception {
        // Redirect System.err to capture output from the servlet
        System.setErr(new PrintStream(errContent));

        servletContext = new MockServletContext();
        forgotPasswordService = new MockForgotPasswordService();
        
        // Put the mock service into the servlet context
        servletContext.setAttribute("ForgotPasswordService", forgotPasswordService);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        // Initialize the servlet with the mock servlet context
        servlet = new ForgotPasswordServlet();
        servlet.init(new MockServletConfig(servletContext));
    }

    @After
    public void restoreStreams() {
        // Restore the original System.err after each test
        System.setErr(originalErr);
    }
    
    // --- Test Cases for doPost method ---

    @Test
    public void testDoPost_MissingEmail_shouldForwardWithError() throws ServletException, IOException {
        // Arrange
        request.setParameter("email", "");

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(PagePaths.FORGOT_PASSWORD_PAGE, response.getForwardedUrl());
        assertEquals("Please enter your email.", request.getAttribute(AttributeKeys.ERROR));
    }

    @Test
    public void testDoPost_UserNotFound_shouldForwardWithError() throws ServletException, IOException {
        // Arrange
        String email = "test@example.com";
        request.setParameter("email", email);
        // This assumes your MockHttpServletRequest has a setServerInfo method
        request.setServerInfo("http", "localhost", 8080, "/app");

        // Mock the service to return false, simulating user not found
        forgotPasswordService.setSuccess(false);

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(PagePaths.FORGOT_PASSWORD_PAGE, response.getForwardedUrl());
        assertEquals("No account found with that email.", request.getAttribute(AttributeKeys.ERROR));
        assertEquals(email, forgotPasswordService.getCapturedEmail());
    }

    @Test
    public void testDoPost_Success_shouldForwardWithSuccessMessage() throws ServletException, IOException {
        // Arrange
        String email = "test@example.com";
        request.setParameter("email", email);
        // This assumes your MockHttpServletRequest has a setServerInfo method
        request.setServerInfo("http", "localhost", 8080, "/app");

        // Mock the service to return true, simulating success
        forgotPasswordService.setSuccess(true);

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(PagePaths.FORGOT_PASSWORD_PAGE, response.getForwardedUrl());
        assertEquals("Password reset link has been sent to your email.", request.getAttribute(AttributeKeys.SUCCESS));
        assertEquals(email, forgotPasswordService.getCapturedEmail());
    }
    
    @Test
    public void testDoPost_ServiceException_shouldForwardWithInternalError() throws ServletException, IOException {
        // Arrange
        String email = "test@example.com";
        request.setParameter("email", email);
        // This assumes your MockHttpServletRequest has a setServerInfo method
        request.setServerInfo("http", "localhost", 8080, "/app");

        // Mock the service to throw an exception
        forgotPasswordService.setThrowException(true);

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(PagePaths.FORGOT_PASSWORD_PAGE, response.getForwardedUrl());
        assertEquals("Error processing password reset request.", request.getAttribute(AttributeKeys.ERROR));
    }

    // --- Custom Mock Class specific to this test ---
    private static class MockForgotPasswordService extends ForgotPasswordService {
        private boolean success = false;
        private String capturedEmail;
        private boolean throwException = false;

        // Add a constructor that calls the superclass's constructor
        public MockForgotPasswordService() {
            super(null, null, null);
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public void setThrowException(boolean throwException) {
            this.throwException = throwException;
        }

        public String getCapturedEmail() {
            return capturedEmail;
        }

        @Override
        public boolean processForgotPassword(String email, String baseUrl) throws Exception {
            this.capturedEmail = email;
            if (throwException) {
                throw new Exception("Mock Service Exception");
            }
            return success;
        }
    }
}