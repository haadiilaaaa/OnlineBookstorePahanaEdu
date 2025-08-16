package customer;

import dto.UserSession;
import handler.customer.BookBrowseHandler;
import mockhttp.MockHttpServletRequest;
import mockhttp.MockHttpServletResponse;
import mockhttp.MockHttpSession;
import mockhttp.MockServletContext;
import mockhttp.MockRequestDispatcher;
import mockhttp.MockServletConfig;
import org.junit.Before;
import org.junit.Test;
import javax.servlet.ServletException;
import java.io.IOException;
import controller.customer.BookBrowseServlet;
import static org.junit.Assert.*;
import static util.contannts.AttributeKeys.ERROR;
import static util.contannts.PagePaths.ERROR_PAGE;
import service.customer.*;
import service.admin.*;
import service.common.*;
import model.*;
import dto.*;
import java.util.*;
import javax.servlet.http.HttpSession;

/**
 * Unit tests for the BookBrowseServlet class.
 * This class uses mock objects to simulate the servlet container environment,
 * allowing us to test the servlet's logic in isolation.
 */
public class BookBrowseServletTest {

    private BookBrowseServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;
    private MockServletContext servletContext;
    private FakeBookBrowseHandler fakeBrowseHandler;
    private UserSession testUserSession;

    /**
     * A fake implementation of the BookBrowseHandler for testing purposes.
     * It allows us to track if the handleRequest method was called and
     * to simulate exceptions for error handling tests.
     */
    private static class FakeBookBrowseHandler extends BookBrowseHandler {
        private boolean wasHandled = false;
        private boolean shouldThrowException = false;

        // Correct constructor to match the superclass constructor
        public FakeBookBrowseHandler(ItemService itemService, CategoryCache categoryCache, SessionCartService sessionCartService) {
            super(itemService, categoryCache, sessionCartService);
        }

        @Override
        public void handleRequest(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws IOException, ServletException {
            this.wasHandled = true;
            if (shouldThrowException) {
                throw new ServletException("Simulated handler exception.");
            }
            // Do nothing else, as the real handler's logic is not the focus of this test.
        }
        
        public boolean wasHandled() {
            return wasHandled;
        }

        public void setShouldThrowException(boolean shouldThrowException) {
            this.shouldThrowException = shouldThrowException;
        }
    }
    
     private static class MockItemService implements ItemService {
        // Implement abstract methods to satisfy the interface.
        @Override
        public List<ItemDTO> searchItems(String keyword, String category, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
            return List.of();
        }

        @Override
        public ItemDTO findById(String id) {
            return new ItemDTO();
        }

        @Override
        public void deleteItem(String id) {}

        @Override
        public void updateItem(ItemDTO dto) {}

        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        public ItemDTO getItemById(String id) {
            return new ItemDTO();
        }

        @Override
        public List<ItemDTO> getItemsByCategory(String categoryId) {
            return List.of();
        }

        @Override
        public List<ItemDTO> getAllItems() {
            return List.of();
        }
        @Override
        public void addItem(ItemDTO dto) {}
    }

    // Corrected: Added the missing MockCategoryService class
    private static class MockCategoryService implements CategoryService {
        @Override
        public List<CategoryDTO> getAllCategories() {
            return new ArrayList<>();
        }
        
       

        @Override
        public void deleteCategory(String category) { }
    
        @Override
        public int getLastCategoryIdNumber() {
            return 0;
        }
        
        @Override
        public boolean isNameExists(String name) {
            return false;
        }
        
        @Override
        public void updateCategory(CategoryDTO categoryDTO) {
             // Mock implementation
        }
    
        @Override
        public CategoryDTO getCategoryById(String id) {
            return new CategoryDTO();
        }
        
        @Override
        public void addCategory(CategoryDTO categoryDTO) {
            // Mock implementation
        }
    }

    // Corrected: Added the missing MockCategoryCache class
    private static class MockCategoryCache extends CategoryCache {
        public MockCategoryCache(CategoryService categoryService, javax.servlet.ServletContext servletContext) {
            super(categoryService, servletContext);
        }

        // Added a default constructor to fix compilation in some scenarios
        public MockCategoryCache() {
            super(null, null);
        }
    }

    /**
     * Mock class for the SessionCartService dependency.
     */
    private static class MockSessionCartService implements SessionCartService {
        @Override
        public void clearCartInSession(HttpSession session) {}
        
        @Override
        public void removeCartItemFromSession(HttpSession session, String itemId) {}
        
        @Override
        public void updateCartItemQuantityInSession(HttpSession session, String itemId, int quantity) {}

        @Override
        public void addToCartInSession(HttpSession session, String itemId, String itemName, java.math.BigDecimal unitPrice, int quantity, String imageUrl, java.math.BigDecimal totalPrice) {}

        @Override
        public void loadCartForUser(HttpSession session, String userId) {}
    }

    @Before
    public void setUp() throws ServletException {
        // Initialize mock objects for each test run.
        request = new MockHttpServletRequest() {
            @Override
            public javax.servlet.RequestDispatcher getRequestDispatcher(String path) {
                return new MockRequestDispatcher(path);
            }
        };
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        servletContext = new MockServletContext();

        // Create a mock user session.
        testUserSession = new UserSession("user-123", "testuser", "test@example.com", "customer", "Test", "User", "123 Test St", "0123456789");
        
        // Create mock dependencies for the handler.
        MockItemService mockItemService = new MockItemService();
        MockCategoryService mockCategoryService = new MockCategoryService(); // Corrected: Instantiated MockCategoryService
        MockCategoryCache mockCategoryCache = new MockCategoryCache(mockCategoryService, servletContext); // Corrected: Correct constructor call
        MockSessionCartService mockSessionCartService = new MockSessionCartService();

        // Create our fake handler, passing the mock dependencies to its constructor.
        fakeBrowseHandler = new FakeBookBrowseHandler(mockItemService, mockCategoryCache, mockSessionCartService);

        // Inject the mock handler into the servlet context, as the servlet's init() method expects it.
        servletContext.setAttribute("BookBrowseHandler", fakeBrowseHandler);

        // Create an instance of the servlet, overriding the getAuthenticatedUser method
        // so we can control the user session for testing.
        servlet = new BookBrowseServlet() {
            @Override
            public javax.servlet.ServletContext getServletContext() {
                return servletContext;
            }

            @Override
            protected UserSession getAuthenticatedUser(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) {
                return testUserSession;
            }
        };

        // Initialize the servlet with a mock config.
        MockServletConfig config = new MockServletConfig(servletContext);
        servlet.init(config);
    }
    
    /**
     * Tests that the servlet successfully calls the handler when a user is authenticated.
     */
    @Test
    public void testServletCallsHandlerOnSuccessfulGet() throws ServletException, IOException {
        // Execute the servlet's doGet method.
        servlet.service(request, response);

        // Verify that the handler's business logic was executed.
        assertTrue("Handler should have been called.", fakeBrowseHandler.wasHandled());

        // Verify that no error message was set on the request.
        assertNull("No error message should be set on success.", request.getAttribute(ERROR));
        
        // The handler is responsible for forwarding, so we check that no forwarding was done by the servlet itself.
        assertNull("Servlet should not forward on success.", response.getForwardedUrl());
        assertNull("Servlet should not redirect on success.", response.getRedirectedUrl());
    }

    /**
     * Tests that the servlet handles an exception from the handler and forwards to the error page.
     */
    @Test
    public void testServletHandlesHandlerExceptionAndForwardsToErrorPage() throws ServletException, IOException {
        // Configure the fake handler to throw an exception.
        fakeBrowseHandler.setShouldThrowException(true);

        // Execute the servlet's doGet method.
        servlet.service(request, response);

        // Verify that the handler was called and the servlet's catch block was triggered.
        assertTrue("Handler should have been called.", fakeBrowseHandler.wasHandled());

        // Verify that an error message was set.
        assertNotNull("An error message should be set on exception.", request.getAttribute(ERROR));
        assertEquals("Failed to load books.", request.getAttribute(ERROR));

        // Verify that the request was forwarded to the error page.
        assertEquals("Request should be forwarded to the error page.", ERROR_PAGE, response.getForwardedUrl());
        assertNull("Request should not be redirected.", response.getRedirectedUrl());
    }

    /**
     * Tests that the servlet returns immediately and does not call the handler for an unauthenticated user.
     */
    @Test
    public void testServletDoesNotCallHandlerForUnauthenticatedUser() throws ServletException, IOException {
        // Create a new servlet instance where getAuthenticatedUser returns null.
        BookBrowseServlet unauthenticatedServlet = new BookBrowseServlet() {
            @Override
            public javax.servlet.ServletContext getServletContext() {
                return servletContext;
            }
            @Override
            protected UserSession getAuthenticatedUser(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) {
                return null;
            }
        };
        unauthenticatedServlet.init(new MockServletConfig(servletContext));
        
        // Execute the servlet's doGet method.
        unauthenticatedServlet.service(request, response);
        
        // Verify that the handler was NOT called, as the servlet should have returned early.
        assertFalse("Handler should not be called for an unauthenticated user.", fakeBrowseHandler.wasHandled());
        
        // Verify no error message was set by the servlet's try-catch block.
        assertNull("No error should be set for an unauthenticated user.", request.getAttribute(ERROR));
    }
    
    /**
     * Tests that the init() method throws a ServletException when the required
     * dependency (BookBrowseHandler) is missing from the ServletContext.
     */
    @Test
    public void testInitThrowsExceptionWhenBookBrowseHandlerIsMissing() {
        MockServletContext missingDepContext = new MockServletContext();
        BookBrowseServlet newServlet = new BookBrowseServlet();
        
        try {
            newServlet.init(new MockServletConfig(missingDepContext));
            fail("ServletException was not thrown for missing dependencies.");
        } catch (ServletException e) {
            assertEquals("BookBrowseHandler not initialized in ServletContext.", e.getMessage());
        }
    }
}
