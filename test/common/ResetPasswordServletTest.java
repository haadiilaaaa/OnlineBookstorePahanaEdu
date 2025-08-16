package common;

import org.junit.Before;
import org.junit.Test;
import service.common.ResetPasswordService;
import util.MessageResolver;
import util.contannts.AttributeKeys;
import util.contannts.ParameterKeys;
import util.contannts.PagePaths;
import util.contannts.SessionKeys;
import mockhttp.*;
import controller.ResetPasswordServlet;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ResetPasswordServletTest {

    private ResetPasswordServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletContext servletContext;
    private MockResetPasswordService mockResetPasswordService;

    @Before
    public void setUp() throws Exception {
        // Initialize mock objects
        servletContext = new MockServletContext();
        mockResetPasswordService = new MockResetPasswordService();

        // Put the mock service into the servlet context for the servlet to find
        servletContext.setAttribute("ResetPasswordService", mockResetPasswordService);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setMethod("POST");

        // Initialize the servlet
        servlet = new ResetPasswordServlet();
        servlet.init(new MockServletConfig(servletContext));
        
        // This line is now removed because MessageResolver is not configurable
        // MessageResolver.setBundle("messages");
    }

    @Test
    public void testDoPost_Success_shouldRedirectToLoginPageWithSuccessMessage() throws ServletException, IOException {
        // Arrange
        request.setParameter(ParameterKeys.TOKEN, "validToken");
        request.setParameter(ParameterKeys.NEW_PASSWORD, "NewPassword123!");
        request.setParameter(ParameterKeys.CONFIRM_PASSWORD, "NewPassword123!");

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(PagePaths.LOGIN_PAGE, response.getRedirectedUrl());
        assertNotNull(request.getSession(false));
        assertEquals(MessageResolver.get("reset.success"), request.getSession(false).getAttribute(SessionKeys.SUCCESS_MESSAGE));
    }

    @Test
    public void testDoPost_InvalidPasswords_shouldForwardWithError() throws ServletException, IOException {
        // Arrange
        String token = "validToken";
        String errorMessage = "Passwords do not match.";
        mockResetPasswordService.setExceptionToThrow(new IllegalArgumentException(errorMessage));

        request.setParameter(ParameterKeys.TOKEN, token);
        request.setParameter(ParameterKeys.NEW_PASSWORD, "passwordA");
        request.setParameter(ParameterKeys.CONFIRM_PASSWORD, "passwordB");

        // Act
        servlet.service(request, response);

        // Assert
        assertNull(response.getRedirectedUrl());
        assertEquals(PagePaths.RESET_PASSWORD_PAGE, response.getForwardedUrl());
        assertEquals(errorMessage, request.getAttribute(AttributeKeys.ERROR));
        assertEquals(token, request.getAttribute(ParameterKeys.TOKEN));
    }

    @Test
    public void testDoPost_ServiceInternalError_shouldForwardWithInternalError() throws ServletException, IOException {
        // Arrange
        String token = "validToken";
        mockResetPasswordService.setExceptionToThrow(new RuntimeException("Database connection failed"));

        request.setParameter(ParameterKeys.TOKEN, token);
        request.setParameter(ParameterKeys.NEW_PASSWORD, "NewPassword123!");
        request.setParameter(ParameterKeys.CONFIRM_PASSWORD, "NewPassword123!");
        
        // Act
        servlet.service(request, response);

        // Assert
        assertNull(response.getRedirectedUrl());
        assertEquals(PagePaths.RESET_PASSWORD_PAGE, response.getForwardedUrl());
        assertEquals(MessageResolver.get("reset.internal_error"), request.getAttribute(AttributeKeys.ERROR));
        assertEquals(token, request.getAttribute(ParameterKeys.TOKEN));
    }

    // --- Mock Classes ---

    private static class MockResetPasswordService implements ResetPasswordService {
    private Exception exceptionToThrow = null;

    // A mock constructor for the interface
    public MockResetPasswordService() {
    }

    public void setExceptionToThrow(Exception e) {
        this.exceptionToThrow = e;
    }

    @Override
    public boolean resetPassword(String token, String newPassword, String confirmPassword) throws Exception {
        if (exceptionToThrow != null) {
            throw exceptionToThrow;
        }
        // An interface method must return a value
        return true;
    }
}
}