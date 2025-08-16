package customer;

import dto.UserSession;
import dto.AddToCartRequestDTO;
import service.common.Validator;
import util.ValidationException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import mockhttp.*;
import service.customer.*;
import service.admin.*;
import model.Item;
import mapper.*;
import model.*;
import java.util.*;
import controller.customer.AddToCartServlet;
import command.customer.cart.AddToCartCommand;
import service.admin.ItemService;
import service.customer.CartFacade;
import dto.ItemDTO;
import static org.junit.Assert.*;
import static util.contannts.PagePaths.*;
import static util.contannts.ParameterKeys.*;
import static util.contannts.SessionKeys.SUCCESS_MESSAGE;
import static util.contannts.AttributeKeys.ERROR_MESSAGE;
import static util.contannts.ErrorMessages.CART_LOAD_FAILED;

public class AddToCartServletTest {

    private AddToCartServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;
    private MockServletContext servletContext;
    private FakeAddToCartCommand fakeAddToCartCommand;
    private FakeValidator fakeValidator;
    private UserSession testUserSession;

    @Before
    public void setUp() throws ServletException {
        // Initialize servlet and mock objects
        request = new MockHttpServletRequest() {
            @Override
            public RequestDispatcher getRequestDispatcher(String path) {
                return new MockRequestDispatcher(path);
            }
        };
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        servletContext = new MockServletContext();
        
        testUserSession = new UserSession("user-123", "testuser", "test@example.com", "customer", "Test", "User", "123 Test St", "0123456789");

        // Initialize our fake dependencies
        fakeValidator = new FakeValidator();
        MockItemService mockItemService = new MockItemService();
        
        // Create mocks for the new CartFacade constructor dependencies
        MockPersistentCartService mockPersistentCartService = new MockPersistentCartService();
        MockSessionCartService mockSessionCartService = new MockSessionCartService();
        MockDiscountService mockDiscountService = new MockDiscountService();
        MockItemMapper mockItemMapper = new MockItemMapper();
        MockCartFacade mockCartFacade = new MockCartFacade(mockPersistentCartService, mockSessionCartService, mockDiscountService, mockItemMapper);
        
        // Pass the mock dependencies to the FakeAddToCartCommand constructor
        fakeAddToCartCommand = new FakeAddToCartCommand(fakeValidator, mockItemService, mockCartFacade);
        
        // Inject dependencies into the servlet context
        servletContext.setAttribute("AddToCartCommand", fakeAddToCartCommand);
        servletContext.setAttribute("AddToCartRequestValidator", fakeValidator);

        // Set up the session and context for the request
        request.setMethod("POST"); // Set the HTTP method explicitly
        request.setSession(session);
        MockServletConfig config = new MockServletConfig(servletContext);
        
        this.servlet = new AddToCartServlet() {
            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }

            @Override
            protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) {
                return testUserSession;
            }
        };

        this.servlet.init(config);
    }

    @Test
    public void testDoPost_Success() throws Exception {
        request.setParameter(ITEM_ID, "1");
        request.setParameter(QUANTITY, "2");

        servlet.service(request, response);

        assertEquals("Item added to cart successfully.", session.getAttribute(SUCCESS_MESSAGE));
        assertEquals(BOOK_BROWSE_SERVLET, response.getRedirectedUrl());
        assertNull(request.getAttribute(ERROR_MESSAGE));
        assertTrue(fakeAddToCartCommand.isExecuted());
    }

    @Test
    public void testDoPost_ValidationException() throws Exception {
        request.setParameter(ITEM_ID, "invalid");
        request.setParameter(QUANTITY, "abc");
        fakeAddToCartCommand.setThrowValidationException(true);

        servlet.service(request, response);

        assertEquals("Invalid input for cart item.", request.getAttribute(ERROR_MESSAGE));
        assertEquals(BOOK_BROWSE_PAGE, response.getForwardedUrl());
        assertNull(response.getRedirectedUrl());
        assertTrue(fakeAddToCartCommand.isExecuted());
    }

    @Test
    public void testDoPost_GenericException() throws Exception {
        request.setParameter(ITEM_ID, "1");
        request.setParameter(QUANTITY, "2");
        fakeAddToCartCommand.setThrowGenericException(true);
        
        servlet.service(request, response);

        assertEquals(CART_LOAD_FAILED, request.getAttribute(ERROR_MESSAGE));
        assertEquals(BOOK_BROWSE_PAGE, response.getForwardedUrl());
        assertNull(response.getRedirectedUrl());
        assertTrue(fakeAddToCartCommand.isExecuted());
    }

    @Test
    public void testDoPost_UnauthenticatedUser() throws Exception {
        AddToCartServlet unauthenticatedServlet = new AddToCartServlet() {
            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }

            @Override
            protected UserSession getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) {
                return null;
            }
        };
        unauthenticatedServlet.init(new MockServletConfig(servletContext));
        
        unauthenticatedServlet.service(request, response);

        assertFalse(fakeAddToCartCommand.isExecuted());
        assertNull(response.getRedirectedUrl());
        assertNull(session.getAttribute(SUCCESS_MESSAGE));
        assertNull(request.getAttribute(ERROR_MESSAGE));
    }

    @Test
    public void testInit_MissingDependencies_shouldThrowException() throws Exception {
        MockServletContext missingDepContext = new MockServletContext();
        AddToCartServlet newServlet = new AddToCartServlet();
        
        try {
            newServlet.init(new MockServletConfig(missingDepContext));
            fail("ServletException was not thrown for missing dependencies.");
        } catch (ServletException e) {
            assertEquals("Failed to initialize AddToCartServlet due to missing dependencies", e.getMessage());
        }
    }
    
    /**
     * Fake command for unit tests, allowing us to control its behavior.
     * It extends the real AddToCartCommand and calls its constructor.
     */
    private static class FakeAddToCartCommand extends AddToCartCommand {
        private boolean throwValidationException = false;
        private boolean throwGenericException = false;
        private boolean wasExecuted = false;

        public FakeAddToCartCommand(Validator<AddToCartRequestDTO> validator, ItemService itemService, CartFacade cartFacade) {
            super(validator, itemService, cartFacade);
        }

        public void setThrowValidationException(boolean value) {
            this.throwValidationException = value;
        }

        public void setThrowGenericException(boolean value) {
            this.throwGenericException = value;
        }

        public boolean isExecuted() {
            return wasExecuted;
        }

        @Override
        public void execute(AddToCartRequestDTO dto, UserSession user, HttpSession httpSession) throws ValidationException {
            this.wasExecuted = true;
            if (throwValidationException) {
                throw new ValidationException("Invalid input for cart item.");
            }
            if (throwGenericException) {
                throw new RuntimeException("Simulated database error.");
            }
        }
    }
    
    /**
     * Fake validator for unit tests.
     */
   private static class FakeValidator implements Validator<AddToCartRequestDTO> {
        @Override
        public void validate(AddToCartRequestDTO dto) throws ValidationException {
            // Do nothing, assume validation always passes.
        }
    }

    /**
     * Simple mock for ItemService, which is likely a concrete class.
     * It extends the real ItemService and implements its abstract methods.
     */
    private static class MockItemService implements ItemService {
        @Override
        public List<ItemDTO> searchItems(String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice) {
            return List.of();
        }
        
        @Override
        public ItemDTO findById(String id) {
            return new ItemDTO(); // Return a mock DTO
        }
        
        @Override
        public void deleteItem(String id) {
            // Placeholder for the deleteItem method
        }

        @Override
        public void updateItem(ItemDTO dto) {
            // Placeholder for the updateItem method
        }
        
        @Override
        public int getItemCount() {
            return 0; // Placeholder for the getItemCount method
        }

        @Override
        public ItemDTO getItemById(String id) {
            return new ItemDTO(); // Placeholder for getItemById
        }
        
        @Override
        public List<ItemDTO> getItemsByCategory(String categoryId) {
            return List.of(); // Placeholder for getItemsByCategory
        }
        
        @Override
        public List<ItemDTO> getAllItems() {
            return List.of(); // Placeholder for getAllItems
        }

        @Override
        public void addItem(ItemDTO dto) {
            // Placeholder for addItem
        }
    }
    
    private static class MockCartFacade extends CartFacade {
        public MockCartFacade(PersistentCartService persistentCartService, SessionCartService sessionCartService, DiscountService discountService, ItemMapper itemMapper) {
            super(persistentCartService, sessionCartService, discountService, itemMapper);
        }
    }
   private static class MockPersistentCartService implements PersistentCartService {
    // Other methods you already have...
    @Override
    public void clearCart(String userId) {
        // Placeholder implementation
    }

    @Override
    public void removeCartItem(String userId, String itemId) {
        // Placeholder implementation
    }
    
    @Override
    public void updateCartItemQuantity(String userId, String itemId, int quantity) {
        // Placeholder implementation
    }

    @Override
    public void addCartItem(String userId, String itemId, int quantity, BigDecimal unitPrice) {
        // Placeholder implementation
    }

    @Override
    public Map<String, CartItem> getCartMapByCustomerId(String userId) {
        // Placeholder implementation
        return new HashMap<>();
    }
    
    // ✅ Add this missing method implementation
    @Override
public CartItem findByCustomerAndItem(String userId, String itemId) {
    // Return a mock CartItem for testing purposes, or null
    // if no item is found.
    return null;
}
}
    
    
   private static class MockSessionCartService implements SessionCartService {
        // Implemented abstract methods
        @Override
        public void clearCartInSession(HttpSession session) {
            // Placeholder implementation
        }

        @Override
        public void removeCartItemFromSession(HttpSession session, String itemId) {
            // Placeholder implementation
        }
        
        @Override
        public void updateCartItemQuantityInSession(HttpSession session, String itemId, int quantity) {
            // Placeholder implementation
        }

        @Override
        public void addToCartInSession(HttpSession session, String itemId, String itemName, BigDecimal unitPrice, int quantity, String imageUrl, BigDecimal totalPrice) {
            // Placeholder implementation
        }
        
        @Override
        public void loadCartForUser(HttpSession session, String userId) {
            // Placeholder implementation
        }
    }
   private static class MockDiscountService implements DiscountService {
        // Implemented abstract methods
        @Override
        public BigDecimal applyBestDiscount(Item item) {
            // Placeholder implementation
            return BigDecimal.ZERO;
        }
    }
    private static class MockItemMapper extends ItemMapper {
        
    }
   
}