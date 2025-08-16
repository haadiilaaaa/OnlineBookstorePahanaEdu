package customer;

import dto.ItemDTO;
import dto.CategoryDTO;
import service.admin.CategoryService;
import service.common.CategoryCache;
import service.admin.ItemService;
import service.customer.SessionCartService;
import mockhttp.MockHttpServletRequest;
import mockhttp.MockHttpServletResponse;
import mockhttp.MockHttpSession;
import mockhttp.MockServletContext;
import org.junit.Before;
import org.junit.Test;
import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static util.contannts.AttributeKeys.ALL_CATEGORIES;
import static util.contannts.AttributeKeys.BOOKS_LIST;
import util.contannts.AttributeKeys;
import handler.customer.*;
import dto.UserSession;

/**
 * Unit tests for the BookBrowseHandler class.
 * This class focuses on testing the handler's business logic for various
 * book browsing and searching scenarios.
 */
public class BookBrowseHandlerTest {

    private BookBrowseHandler handler;
    private MockItemService mockItemService;
    private MockCategoryCache mockCategoryCache;
    private MockSessionCartService mockSessionCartService;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    // --- Mock Service Implementations for the Handler ---

    /**
     * A mock implementation of ItemService to simulate different search results.
     * The searchItems method has been updated to search by both item name and author.
     */
    private static class MockItemService implements ItemService {
        private List<ItemDTO> items;
        private boolean searchCalled = false;
        private String lastKeyword;
        private String lastCategory;
        private BigDecimal lastMinPrice;
        private BigDecimal lastMaxPrice;

        public MockItemService(List<ItemDTO> items) {
            this.items = new ArrayList<>(items);
        }

        @Override
        public List<ItemDTO> searchItems(String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice) throws Exception {
            this.searchCalled = true;
            this.lastKeyword = keyword;
            this.lastCategory = category;
            this.lastMinPrice = minPrice;
            this.lastMaxPrice = maxPrice;

            // Updated mock logic: filter based on keyword (item name or author), category, and price range
            return items.stream()
                .filter(item -> keyword == null || 
                         item.getTitle().toLowerCase().contains(keyword.toLowerCase()) || 
                         item.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
                .filter(item -> category == null || item.getCategoryName().equalsIgnoreCase(category))
                .filter(item -> minPrice == null || item.getPrice().compareTo(minPrice) >= 0)
                .filter(item -> maxPrice == null || item.getPrice().compareTo(maxPrice) <= 0)
                .toList();
        }

        // Other methods are not directly tested in this handler, but we need to implement them
        @Override public ItemDTO findById(String id) throws Exception { return null; }
        @Override public void deleteItem(String id) throws Exception {}
        @Override public void updateItem(ItemDTO dto) throws Exception {}
        @Override public int getItemCount() throws Exception { return items.size(); }
        @Override public ItemDTO getItemById(String id) throws Exception { return null; }
        @Override public List<ItemDTO> getItemsByCategory(String categoryId) throws Exception { return Collections.emptyList(); }
        @Override public List<ItemDTO> getAllItems() throws Exception { return items; }
        @Override public void addItem(ItemDTO dto) throws Exception {}
    }
    
    /**
     * A mock implementation of CategoryService for testing.
     * It simply returns a predefined list of categories and provides a default
     * implementation for the other methods.
     */
    private static class MockCategoryService implements CategoryService {
        private final List<CategoryDTO> categories;

        public MockCategoryService(List<CategoryDTO> categories) {
            this.categories = categories;
        }

        @Override
        public List<CategoryDTO> getAllCategories() throws Exception {
            return new ArrayList<>(this.categories);
        }

        @Override
        public void addCategory(CategoryDTO category) throws Exception {}

        @Override
        public void updateCategory(CategoryDTO category) throws Exception {}

        @Override
        public CategoryDTO getCategoryById(String categoryId) throws Exception { return null; }
        
        @Override
        public void deleteCategory(String categoryId) throws Exception {}
        
        @Override
        public int getLastCategoryIdNumber() throws Exception {
            return 0; // Return a default value as it's not needed for this test.
        }

        @Override
        public boolean isNameExists(String name) throws Exception {
            return categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(name));
        }
    }

    /**
     * A mock implementation for CategoryCache.
     */
     private static class MockCategoryCache extends CategoryCache {
    private List<CategoryDTO> allCategories;
    private final javax.servlet.ServletContext context;

    public MockCategoryCache(CategoryService categoryService, javax.servlet.ServletContext context) throws Exception {
        super(categoryService, context);
        this.context = context;
        this.allCategories = categoryService.getAllCategories();
        context.setAttribute(ALL_CATEGORIES, this.allCategories);
    }
    
    @Override
    public List<CategoryDTO> getCategories() {
        return (List<CategoryDTO>) context.getAttribute(ALL_CATEGORIES);
    }
}

    /**
     * A simple mock for SessionCartService.
     */
    private static class MockSessionCartService implements SessionCartService {
        // Implement all methods, but they can be empty as the handler doesn't
        // interact with them directly for browsing.
        @Override public void clearCartInSession(javax.servlet.http.HttpSession session) {}
        @Override public void removeCartItemFromSession(javax.servlet.http.HttpSession session, String itemId) {}
        @Override public void updateCartItemQuantityInSession(javax.servlet.http.HttpSession session, String itemId, int quantity) {}
        @Override public void addToCartInSession(javax.servlet.http.HttpSession session, String itemId, String itemName, BigDecimal unitPrice, int quantity, String imageUrl, BigDecimal totalPrice) {}
        @Override public void loadCartForUser(javax.servlet.http.HttpSession session, String userId) {}
    }

  
     // Corrected setUp method
// Corrected setUp method
@Before
public void setUp() throws Exception {
    // Initialize with sample data for our tests
    List<ItemDTO> sampleItems = new ArrayList<>();

    // Using setters to create ItemDTO objects
    ItemDTO item1 = new ItemDTO();
    item1.setId("item-001");
    item1.setTitle("The Hitchhiker's Guide to the Galaxy");
    item1.setCategoryName("Fantasy");
    item1.setAuthor("Douglas Adams");
    item1.setPrice(new BigDecimal("10.99"));
    item1.setImageUrl("image1.jpg");
    sampleItems.add(item1);

    ItemDTO item2 = new ItemDTO();
    item2.setId("item-002");
    item2.setTitle("The Lord of the Rings");
    item2.setCategoryName("Fantasy");
    item2.setAuthor("J.R.R. Tolkien");
    item2.setPrice(new BigDecimal("25.50"));
    item2.setImageUrl("image2.jpg");
    sampleItems.add(item2);

    ItemDTO item3 = new ItemDTO();
    item3.setId("item-003");
    item3.setTitle("Pride and Prejudice");
    item3.setCategoryName("Romance");
    item3.setAuthor("Jane Austen");
    item3.setPrice(new BigDecimal("8.75"));
    item3.setImageUrl("image3.jpg");
    sampleItems.add(item3);

    ItemDTO item4 = new ItemDTO();
    item4.setId("item-004");
    item4.setTitle("1984");
    item4.setCategoryName("Science Fiction");
    item4.setAuthor("George Orwell");
    item4.setPrice(new BigDecimal("12.00"));
    item4.setImageUrl("image4.jpg");
    sampleItems.add(item4);

    List<CategoryDTO> sampleCategories = new ArrayList<>();
    CategoryDTO category1 = new CategoryDTO();
    category1.setName("Fantasy");
    category1.setDescription("Fantasy books");
    sampleCategories.add(category1);

    CategoryDTO category2 = new CategoryDTO();
    category2.setName("Romance");
    category2.setDescription("Romance novels");
    sampleCategories.add(category2);

    CategoryDTO category3 = new CategoryDTO();
    category3.setName("Science Fiction");
    category3.setDescription("Science fiction books");
    sampleCategories.add(category3);

    // Instantiate mocks with the sample data
    mockItemService = new MockItemService(sampleItems);
    
    MockServletContext mockServletContext = new MockServletContext();
    MockCategoryService mockCategoryService = new MockCategoryService(sampleCategories);
    mockCategoryCache = new MockCategoryCache(mockCategoryService, mockServletContext);
    
    // IMPORTANT FIX: Ensure the category list is put into the mock servlet context,
    // so it can be retrieved by the handler.
    mockServletContext.setAttribute(ALL_CATEGORIES, sampleCategories);

    mockSessionCartService = new MockSessionCartService();

    // Create the handler with the mock dependencies
    handler = new BookBrowseHandler(mockItemService, mockCategoryCache, mockSessionCartService);

    // Initialize mock request and response objects for each test
    request = new MockHttpServletRequest(); // Pass the context to the request
    response = new MockHttpServletResponse();

    // The handler requires a session, so we add a mock one
    request.setSession(new MockHttpSession(mockServletContext));
    UserSession userSession = new UserSession(
        "test-user-id",    
        "test-username",     
        "test-email",      
        "customer",        
        "John",            
        "Doe",               
        "123-456-7890",    
        "123 Test Street"  
    );
    request.getSession().setAttribute("user", userSession);
}
@Test
public void testHandleRequest_NoSearchParameters() throws ServletException, IOException, Exception {
    handler.handleRequest(request, response);

    // FIX: Get categories from the request using the correct key
    List<CategoryDTO> categories = (List<CategoryDTO>) request.getAttribute(AttributeKeys.CATEGORIES);
    
    assertNotNull("Categories list should not be null.", categories);
    assertEquals("Expected all categories to be returned.", 3, categories.size());

    // The rest of the assertions for the books list and mock calls
    List<ItemDTO> books = (List<ItemDTO>) request.getAttribute(AttributeKeys.ITEMS);
    assertNotNull("Books list should not be null.", books);
    assertEquals("Expected all books to be returned.", 4, books.size());

    assertTrue("ItemService search method should have been called.", mockItemService.searchCalled);
    assertEquals("Keyword should be null.", null, mockItemService.lastKeyword);
    assertEquals("Category should be null.", null, mockItemService.lastCategory);
}
   @Test
public void testHandleRequest_SearchByKeyword() throws ServletException, IOException, Exception {
    request.setParameter("keyword", "hitchhiker");

    handler.handleRequest(request, response);

    // FIX: Change the attribute key from BOOKS_LIST to ITEMS
    List<ItemDTO> books = (List<ItemDTO>) request.getAttribute(AttributeKeys.ITEMS);

    assertNotNull("Books list should not be null.", books);
    assertEquals("Expected 1 book for keyword 'hitchhiker'.", 1, books.size());
    assertEquals("Book name should match.", "The Hitchhiker's Guide to the Galaxy", books.get(0).getTitle());

    // Verify ItemService was called with the correct keyword
    assertEquals("ItemService search should be called with correct keyword.", "hitchhiker", mockItemService.lastKeyword);
}
    /**
     * Tests searching for books by a specific category.
     */
    @Test
public void testHandleRequest_SearchByCategory() throws ServletException, IOException, Exception {
    request.setParameter("category", "Fantasy");

    handler.handleRequest(request, response);

    // FIX: Change the attribute key to AttributeKeys.ITEMS
    List<ItemDTO> books = (List<ItemDTO>) request.getAttribute(AttributeKeys.ITEMS);

    assertNotNull("Books list should not be null.", books);
    assertEquals("Expected 2 books for category 'Fantasy'.", 2, books.size());

    // Assert that the returned books belong to the correct category
    assertTrue(books.stream().allMatch(book -> "Fantasy".equalsIgnoreCase(book.getCategoryName())));
}

    /**
     * Tests searching for books within a specific price range.
     */
   @Test
public void testHandleRequest_SearchByPriceRange() throws ServletException, IOException, Exception {
    request.setParameter("minPrice", "10.00");
    request.setParameter("maxPrice", "15.00");

    handler.handleRequest(request, response);

    // FIX: Change the attribute key from BOOKS_LIST to AttributeKeys.ITEMS
    List<ItemDTO> books = (List<ItemDTO>) request.getAttribute(AttributeKeys.ITEMS);

    assertNotNull("Books list should not be null.", books);
    assertEquals("Expected 2 books in the price range.", 2, books.size());

    // Assert that the returned books are within the price range
    for (ItemDTO book : books) {
        assertTrue(book.getPrice().compareTo(new BigDecimal("10.00")) >= 0);
        assertTrue(book.getPrice().compareTo(new BigDecimal("15.00")) <= 0);
    }
}

    /**
     * Tests searching for books by a combination of keyword and category.
     */
    @Test
    public void testHandleRequest_SearchByKeywordAndCategory() throws ServletException, IOException, Exception {
        request.setParameter("keyword", "ring");
        handler.handleRequest(request, response);

        List<ItemDTO> books = (List<ItemDTO>) request.getAttribute(AttributeKeys.ITEMS);
        assertNotNull("Books list should not be null.", books);
        assertEquals("Expected 1 book for keyword 'ring' in category 'Fantasy'.", 1, books.size());
        assertEquals("The returned book should be 'The Lord of the Rings'.", "The Lord of the Rings", books.get(0).getTitle());
    }

    /**
     * Tests searching for books by a specific author's name.
     */
    @Test
    public void testHandleRequest_SearchByAuthor() throws ServletException, IOException, Exception {
        request.setParameter("keyword", "austen");

        handler.handleRequest(request, response);

         List<ItemDTO> books = (List<ItemDTO>) request.getAttribute(AttributeKeys.ITEMS);
        assertNotNull("Books list should not be null.", books);
        assertEquals("Expected 1 book for author 'austen'.", 1, books.size());
        assertEquals("The returned book should be 'Pride and Prejudice'.", "Pride and Prejudice", books.get(0).getTitle());
    }
}
