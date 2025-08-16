package customer;

import controller.customer.UpdatecartServlet;
import dto.UserSession;
import mockhttp.*;
import org.junit.Before;
import org.junit.Test;
import service.customer.CartFacade;
import util.contannts.ContextKeys;
import util.contannts.ErrorMessages;
import util.contannts.PagePaths;
import util.contannts.ParameterKeys;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static util.contannts.ErrorMessages.MISSING_PARAMS;
import static util.contannts.PagePaths.CART_PAGE;

/**
 * Unit tests for the UpdatecartServlet class.
 * This class uses mock objects to test the servlet's logic in isolation.
 */
public class UpdateCartServletTest {

    private UpdatecartServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;
    private MockServletContext servletContext;
    private MockCartFacade mockCartFacade;
    private UserSession testUserSession;

    /**
     * A mock implementation of CartFacade for testing purposes.
     * It allows us to simulate the behavior of the real facade.
     */
    private static class MockCartFacade extends CartFacade {
        public boolean updateCartItemCalled = false;
        public String lastUserId;
        public String lastItemId;
        public int lastQuantity;
        public boolean throwException = false;

        public MockCartFacade() {
            super(null, null, null, null);
        }

        @Override
        public void updateCartItem(String userId, String itemId, int quantity, HttpSession session) throws Exception {
            this.updateCartItemCalled = true;
            this.lastUserId = userId;
            this.lastItemId = itemId;
            this.lastQuantity = quantity;
            if (throwException) {
                throw new Exception("Simulated database error.");
            }
        }
    }

   @Before
public void setUp() throws ServletException {
    // Initialize mock objects
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    session = new MockHttpSession();
    servletContext = new MockServletContext();

    // Create a mock CartFacade and inject it into the ServletContext
    mockCartFacade = new MockCartFacade();
    servletContext.setAttribute(ContextKeys.CART_FACADE, mockCartFacade);

    // Set up the session with a mock user
    testUserSession = new UserSession("user-123", "testuser", "test@example.com", "customer", "Test", "User", "123 Test St", "0123456789");
    session.setAttribute("user", testUserSession);

    // Link the request, session, and context
    request.setSession(session);
    MockServletConfig config = new MockServletConfig(servletContext);

    // Create a spy-like servlet instance that uses our mock context and overrides getAuthenticatedUser
    this.servlet = new UpdatecartServlet() {
        @Override
        public ServletContext getServletContext() {
            return servletContext;
        }

        @Override
        protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            return testUserSession;
        }
    };

    // Manually initialize the servlet to use our mock dependencies
    this.servlet.init(config);
}
    // --- Test Cases ---

    @Test
    public void testDoPost_Success() throws Exception {
        // Arrange
        request.setParameter(ParameterKeys.ITEM_ID, "item-001");
        request.setParameter(ParameterKeys.QUANTITY, "5");

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(PagePaths.CART_PAGE + "?success=" + ErrorMessages.CART_UPDATED_SUCCESSFULLY, response.getRedirectedUrl());
        assertNull(response.getForwardedUrl());
        assertNotNull(mockCartFacade);
        assertEquals(testUserSession.getId(), mockCartFacade.lastUserId);
        assertEquals("item-001", mockCartFacade.lastItemId);
        assertEquals(5, mockCartFacade.lastQuantity);
    }

    @Test
    public void testDoPost_MissingParameters() throws Exception {
        // Arrange
        request.setParameter(ParameterKeys.ITEM_ID, "item-001");
        // QUANTITY parameter is missing

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(CART_PAGE + "?error=" + MISSING_PARAMS, response.getRedirectedUrl());
        assertNull(response.getForwardedUrl());
        // Verify that the cart facade was not called
        assertEquals(false, mockCartFacade.updateCartItemCalled);
    }
    
    @Test
    public void testDoPost_InvalidQuantity() throws Exception {
        // Arrange
        request.setParameter(ParameterKeys.ITEM_ID, "item-001");
        request.setParameter(ParameterKeys.QUANTITY, "abc");

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(PagePaths.CART_PAGE + "?success=" + ErrorMessages.CART_UPDATED_SUCCESSFULLY, response.getRedirectedUrl());
        assertEquals(0, mockCartFacade.lastQuantity); // Should default to 0 on parse error
    }

    @Test
    public void testDoPost_NegativeQuantity() throws Exception {
        // Arrange
        request.setParameter(ParameterKeys.ITEM_ID, "item-001");
        request.setParameter(ParameterKeys.QUANTITY, "-5");

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(PagePaths.CART_PAGE + "?success=" + ErrorMessages.CART_UPDATED_SUCCESSFULLY, response.getRedirectedUrl());
        assertEquals(0, mockCartFacade.lastQuantity); // Should default to 0 for negative values
    }

    @Test
    public void testDoPost_UpdateFails_ThrowsException() throws Exception {
        // Arrange
        request.setParameter(ParameterKeys.ITEM_ID, "item-001");
        request.setParameter(ParameterKeys.QUANTITY, "5");
        mockCartFacade.throwException = true; // Make the facade throw an exception

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(PagePaths.CART_PAGE + "?error=" + ErrorMessages.CART_UPDATE_FAILED, response.getRedirectedUrl());
    }

  @Test
public void testDoPost_UnauthenticatedUser() throws Exception {
    // Arrange a servlet that returns a null user session
    UpdatecartServlet unauthenticatedServlet = new UpdatecartServlet() {
        @Override
        public ServletContext getServletContext() {
            return servletContext;
        }
        @Override
        protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            return null;
        }
    };

    // FIX: Add parameters to the request so the servlet proceeds past the initial check.
    request.setParameter(ParameterKeys.ITEM_ID, "item-001");
    request.setParameter(ParameterKeys.QUANTITY, "5");

    // Manually initialize the servlet
    unauthenticatedServlet.init(new MockServletConfig(servletContext));
    
    // Act
    unauthenticatedServlet.service(request, response);

    // Assert
    // The servlet now correctly redirects to the login page as expected.
    assertEquals(PagePaths.LOGIN_PAGE, response.getRedirectedUrl());
    assertNull(response.getForwardedUrl());
    // The servlet should not have called the facade
    assertEquals(false, mockCartFacade.updateCartItemCalled);
}
    @Test
    public void testInit_MissingCartFacade_shouldThrowException() throws Exception {
        // Arrange a servlet context without the facade
        MockServletContext missingDepContext = new MockServletContext();
        UpdatecartServlet newServlet = new UpdatecartServlet();

        try {
            // Act
            newServlet.init(new MockServletConfig(missingDepContext));
            fail("ServletException was not thrown for missing CartFacade.");
        } catch (ServletException e) {
            // Assert
            assertEquals("CartFacade not found in ServletContext.", e.getMessage());
        }
    }
}