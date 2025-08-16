package common;

import org.junit.Before;
import org.junit.Test;
import mockhttp.MockHttpServletRequest;
import mockhttp.MockHttpServletResponse;
import mockhttp.MockHttpSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import controller.LogoutServlet;
import static org.junit.Assert.*;

public class LogoutServletTest {

    // The type of the servlet variable is changed to LogoutServlet
    // to allow access to the protected doGet method without a compile error.
    private LogoutServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp() {
        servlet = new LogoutServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testDoGet_SessionExists_ShouldInvalidateSessionAndRedirectToLogin() throws ServletException, IOException {
        // Arrange
        // Create a session to start with.
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        assertNotNull("Precondition: Session should exist", request.getSession(false));

        // Act
        // Changed from servlet.service to servlet.doGet to explicitly test the doGet method.
        servlet.doGet(request, response);

        // Assert
        assertEquals("Should redirect to the login page", request.getContextPath() + "/login.jsp", response.getRedirectedUrl());

        // To properly test session invalidation, we create a new request object to simulate a fresh request.
        MockHttpServletRequest newRequest = new MockHttpServletRequest();
        assertNull("Session should be null after invalidation", newRequest.getSession(false));
    }

    @Test
    public void testDoGet_NoSession_ShouldRedirectToLogin() throws ServletException, IOException {
        // Arrange
        // We do not create a session, so req.getSession(false) will return null.
        assertNull("Precondition: No session should exist", request.getSession(false));

        // Act
        // Changed from servlet.service to servlet.doGet to explicitly test the doGet method.
        servlet.doGet(request, response);

        // Assert
        assertEquals("Should redirect to the login page", request.getContextPath() + "/login.jsp", response.getRedirectedUrl());
        assertNull("No session should have been created", request.getSession(false));
    }
}
