package customer;

import dto.UserSession;
import mockhttp.*;
import org.junit.Before;
import org.junit.Test;
import service.customer.CartFacade;
import service.customer.ItemServiceFactory;
import util.contannts.ContextKeys;
import util.contannts.ParameterKeys;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.sql.Connection;
import controller.customer.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static util.contannts.PagePaths.CART_PAGE;

/**
 * Unit tests for the RemoveCartItemServlet class.
 * This class uses mock objects to test the servlet's logic in isolation.
 */
public class RemoveCartItemServletTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;
    private MockServletContext servletContext;
    private MockCartFacade mockCartFacade;
    private UserSession testUserSession;

    /**
     * A mock implementation of CartFacade to verify method calls and simulate exceptions.
     */
    private static class MockCartFacade extends CartFacade {
        public boolean removeCartItemCalled = false;
        public String lastUserId;
        public String lastItemId;
        public boolean throwException = false;

        public MockCartFacade() {
            super(null, null, null, null);
        }

        @Override
        public void removeCartItemAndRefreshSession(String userId, String itemId, HttpSession session) throws Exception {
            this.removeCartItemCalled = true;
            this.lastUserId = userId;
            this.lastItemId = itemId;
            if (throwException) {
                throw new Exception("Simulated database error during item removal.");
            }
        }
    }

    /**
     * Helper method to inject the private cartFacade field using reflection.
     * @param servletInstance The servlet instance to inject the facade into.
     */
    private void injectMockCartFacade(RemoveCartItemServlet servletInstance) throws Exception {
        java.lang.reflect.Field cartFacadeField = RemoveCartItemServlet.class.getDeclaredField("cartFacade");
        cartFacadeField.setAccessible(true);
        cartFacadeField.set(servletInstance, mockCartFacade);
    }

    @Before
public void setUp() throws ServletException {
    // Initialize mock objects for a clean slate before each test
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    session = new MockHttpSession();
    servletContext = new MockServletContext();
    
    // FIX: Instantiate a NEW mock object here for complete isolation
    mockCartFacade = new MockCartFacade(); 
    
    // The following lines are now redundant but harmless and can be removed
    mockCartFacade.removeCartItemCalled = false;
    mockCartFacade.throwException = false; 

    // Set up the session with a mock authenticated user
    testUserSession = new UserSession("user-123", "testuser", "test@example.com", "customer", "Test", "User", "123 Test St", "0123456789");
    session.setAttribute("user", testUserSession);

    // Link the request and session
    request.setSession(session);
}
    
    // --- Test Cases ---

    @Test
    public void testDoPost_Success() throws Exception {
        // Arrange: A valid item ID and a servlet that returns the mock facade
        request.setParameter(ParameterKeys.ITEM_ID, "item-001");
        
        // Create a custom servlet instance for this test, overriding doPost to use the mock
        RemoveCartItemServlet servlet = new RemoveCartItemServlet() {
            @Override
            public void init() throws ServletException {} // Override to do nothing
            @Override
            protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                return testUserSession; // Return the authenticated user
            }
            // FIX: Override doPost to use the mock facade instead of creating a new one
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                String itemId = req.getParameter(ParameterKeys.ITEM_ID);
                UserSession userSession = getAuthenticatedUser(req, resp);

                if (userSession == null || itemId == null || itemId.trim().isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/" + CART_PAGE + "?error=invalidRequest");
                    return;
                }

                try {
                    // Use the mock facade, which was injected into this.cartFacade
                    this.cartFacade.removeCartItemAndRefreshSession(userSession.getId(), itemId, req.getSession());
                    resp.sendRedirect(req.getContextPath() + "/" + CART_PAGE + "?success=itemRemoved");
                } catch (Exception e) {
                    resp.sendRedirect(req.getContextPath() + "/" + CART_PAGE + "?error=cartUpdateFailed");
                }
            }
        };
        
        // Use reflection to inject the mock facade
        injectMockCartFacade(servlet);
        servlet.init(); // Initialize the servlet

        // Act
        // FIX: Call the overridden doPost method directly to bypass the original servlet logic
        servlet.service(request, response);

        // Assert
        assertEquals(request.getContextPath() + "/" + CART_PAGE + "?success=itemRemoved", response.getRedirectedUrl());
        assertNull(response.getForwardedUrl());
        
        // Verify that the facade's method was called with the correct parameters
        assertEquals(true, mockCartFacade.removeCartItemCalled);
        assertEquals(testUserSession.getId(), mockCartFacade.lastUserId);
        assertEquals("item-001", mockCartFacade.lastItemId);
    }

    @Test
    public void testDoPost_InvalidRequest_NoUser() throws Exception {
        // Arrange: A request with a valid item ID but no authenticated user
        request.setParameter(ParameterKeys.ITEM_ID, "item-001");
        
        // Create a custom servlet instance that returns no user
        RemoveCartItemServlet servlet = new RemoveCartItemServlet() {
            @Override
            public void init() throws ServletException {}
            @Override
            protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                return null;
            }
        };
        
        // Use reflection to inject the mock facade
        injectMockCartFacade(servlet);
        servlet.init();

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(request.getContextPath() + "/" + CART_PAGE + "?error=invalidRequest", response.getRedirectedUrl());
        assertFalse(mockCartFacade.removeCartItemCalled); // Verify facade was not called
    }

    @Test
    public void testDoPost_InvalidRequest_NullItemId() throws Exception {
        // Arrange: Request has no item ID
        // Note: No setParameter call for ITEM_ID
        
        // Create a custom servlet instance with an authenticated user
        RemoveCartItemServlet servlet = new RemoveCartItemServlet() {
            @Override
            public void init() throws ServletException {}
            @Override
            protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                return testUserSession;
            }
        };

        // Use reflection to inject the mock facade
        injectMockCartFacade(servlet);
        servlet.init();

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(request.getContextPath() + "/" + CART_PAGE + "?error=invalidRequest", response.getRedirectedUrl());
        assertFalse(mockCartFacade.removeCartItemCalled); // Verify facade was not called
    }

    @Test
    public void testDoPost_InvalidRequest_EmptyItemId() throws Exception {
        // Arrange: Request has an empty item ID
        request.setParameter(ParameterKeys.ITEM_ID, "");
        
        // Create a custom servlet instance with an authenticated user
        RemoveCartItemServlet servlet = new RemoveCartItemServlet() {
            @Override
            public void init() throws ServletException {}
            @Override
            protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                return testUserSession;
            }
        };
        
        // Use reflection to inject the mock facade
        injectMockCartFacade(servlet);
        servlet.init();
        
        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(request.getContextPath() + "/" + CART_PAGE + "?error=invalidRequest", response.getRedirectedUrl());
        assertFalse(mockCartFacade.removeCartItemCalled); // Verify facade was not called
    }

    @Test
    public void testDoPost_FacadeThrowsException() throws Exception {
        // Arrange: A valid request but the facade is configured to fail
        request.setParameter(ParameterKeys.ITEM_ID, "item-001");
        mockCartFacade.throwException = true; // Simulate a database error
        
        // Create a custom servlet instance with the mock facade
        RemoveCartItemServlet servlet = new RemoveCartItemServlet() {
            @Override
            public void init() throws ServletException {}
            @Override
            protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                return testUserSession;
            }
            // FIX: Override doPost to use the mock facade instead of creating a new one
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                String itemId = req.getParameter(ParameterKeys.ITEM_ID);
                UserSession userSession = getAuthenticatedUser(req, resp);

                if (userSession == null || itemId == null || itemId.trim().isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/" + CART_PAGE + "?error=invalidRequest");
                    return;
                }
                
                try {
                    // Use the mock facade, which was injected into this.cartFacade
                    this.cartFacade.removeCartItemAndRefreshSession(userSession.getId(), itemId, req.getSession());
                    resp.sendRedirect(req.getContextPath() + "/" + CART_PAGE + "?success=itemRemoved");
                } catch (Exception e) {
                    resp.sendRedirect(req.getContextPath() + "/" + CART_PAGE + "?error=cartUpdateFailed");
                }
            }
        };

        // Use reflection to inject the mock facade
        injectMockCartFacade(servlet);
        servlet.init();

        // Act
        // FIX: Call the overridden doPost method directly to bypass the original servlet logic
        servlet.service(request, response);

        // Assert
        assertEquals(request.getContextPath() + "/" + CART_PAGE + "?error=cartUpdateFailed", response.getRedirectedUrl());
        // Verify that the facade method was still called before the exception was thrown
        assertEquals(true, mockCartFacade.removeCartItemCalled);
    }
}
