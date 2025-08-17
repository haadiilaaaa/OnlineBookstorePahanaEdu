package admin;

import controller.admin.AddItemServlet;
import dto.ItemDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import service.admin.CategoryService;
import service.admin.ItemService;
import strategy.admin.item.ItemActionStrategy;
import strategy.admin.item.StrategyResult;
import util.contannts.ParameterKeys;
import util.contannts.PagePaths;
import util.enums.ItemAction;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mockhttp.*;
import dto.CategoryDTO;
import java.sql.Timestamp;

/**
 * JUnit 4.0 tests for the AddItemServlet.
 * This class includes all necessary mock implementations to run without external dependencies.
 */
@RunWith(JUnit4.class)
public class AddItemServletTest {

    private AddItemServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletContext servletContext;

    private MockItemService mockItemService;
    private MockCategoryService mockCategoryService;
    private Map<String, ItemActionStrategy> mockStrategyMap;

    @Before
    public void setUp() throws ServletException {
        // Initialize the servlet and its mocked environment
        servlet = new AddItemServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        servletContext = new MockServletContext();

        // Initialize mock dependencies
        mockItemService = new MockItemService();
        mockCategoryService = new MockCategoryService();
        mockStrategyMap = new HashMap<>();

        // Add strategies to the map that simulate different actions.
        mockStrategyMap.put(ItemAction.ADD.name(), (req, resp) -> {
            req.setAttribute("message", "Item added successfully!");
            return new StrategyResult(PagePaths.ADMIN_ADD_ITEM_PAGE, false);
        });

         mockStrategyMap.put(ItemAction.EDIT.name(), (req, resp) -> {
            // FIX: Pass a string argument to getItemById to match the method signature
            req.setAttribute("item", mockItemService.getItemById("1"));
            return new StrategyResult(PagePaths.ADMIN_ADD_ITEM_PAGE, false);
        });


        mockStrategyMap.put(ItemAction.DELETE.name(), (req, resp) -> {
            return new StrategyResult(PagePaths.ADMIN_DASHBOARD, true);
        });

        // Set the dependencies as servlet context attributes, simulating web.xml setup
        servletContext.setAttribute("ItemService", mockItemService);
        servletContext.setAttribute("CategoryService", mockCategoryService);
        servletContext.setAttribute("ItemStrategyMap", mockStrategyMap);

        // Call the servlet's init method using a MockServletConfig
        servlet.init(new MockServletConfig(servletContext));
    }

    @Test
    public void testDoGet_InitialView() throws ServletException, IOException {
        // A GET request without an "action" parameter defaults to the regular view.
        servlet.service(request, response);

        // Assert 1: The correct view (JSP page) is forwarded.
        assertEquals(PagePaths.ADMIN_ADD_ITEM_PAGE, request.getForwardedPath());

        // Assert 2: The categories list is loaded for the admin to use in the form.
        assertNotNull(request.getAttribute("categories"));
        assertTrue(!((List<?>) request.getAttribute("categories")).isEmpty());
    }

    @Test
    public void testDoGet_EditAction_PopulatedForm() throws ServletException, IOException {
        // The action parameter in the URL triggers the EDIT strategy.
        request.setParameter(ParameterKeys.ACTION, ItemAction.EDIT.name());
        request.setParameter(ParameterKeys.ITEM_ID, "1");

        servlet.service(request, response);

        // Assert 1: The admin is taken to the add/edit item page.
        assertEquals(PagePaths.ADMIN_ADD_ITEM_PAGE, request.getForwardedPath());

        // Assert 2: The request attribute "item" is not null.
        assertNotNull(request.getAttribute("item"));
    }

    @Test
    public void testDoPost_AddAction_SuccessMessage() throws ServletException, IOException {
        // Test what the admin sees after a successful form submission to add a new item.
        request.setParameter(ParameterKeys.ACTION, ItemAction.ADD.name());
        request.setParameter("itemName", "New Test Item");

        servlet.service(request, response);

        // Assert 1: The admin is still on the same page, but with a success message.
        assertEquals(PagePaths.ADMIN_ADD_ITEM_PAGE, request.getForwardedPath());

        // Assert 2: A success message is set as an attribute.
        assertEquals("Item added successfully!", request.getAttribute("message"));
    }

    @Test
    public void testDoPost_DeleteAction_RedirectsAdmin() throws ServletException, IOException {
        // Test the admin's experience after deleting an item.
        request.setParameter(ParameterKeys.ACTION, ItemAction.DELETE.name());

        servlet.service(request, response);

        // Assert 1: The response is a redirect.
        assertTrue(response.isCommitted());

        // Assert 2: The admin is redirected to the correct URL.
        assertEquals(PagePaths.ADMIN_DASHBOARD, response.getRedirectedUrl());
    }

    //-------------------------------------------------------------------------
    // Inner Mock Classes to satisfy the "cannot find symbol" errors
    //-------------------------------------------------------------------------

    
    private static class MockItemService implements ItemService {
        
          @Override
        public List<ItemDTO> searchItems(String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice) {
            // Dummy implementation for the searchItems method
            return new ArrayList<>();
        }
        
         @Override
        public ItemDTO findById(String id) {
            // Corrected to use a no-argument constructor and setters
            ItemDTO item = new ItemDTO();
            item.setId(id);
            item.setTitle("Found Title");
            item.setAuthor("Found Author");
            item.setDescription("Found Desc");
            item.setPrice(new BigDecimal("20.00"));
            item.setStockQuantity(20);
            item.setImageUrl("found.jpg");
            item.setCategoryId("2");
            item.setCategoryName("Found Category");
            return item;
        }
        
         @Override
        public void deleteItem(String id) {
            // A mock implementation that does nothing, since the return type is void
        }
        
        @Override
        public void updateItem(ItemDTO itemDTO) {
            // A mock implementation that does nothing, since the return type is void
        }
        
        @Override
        public int getItemCount() {
            // A mock implementation that returns a fixed value
            return 10;
        }
        
        @Override
        public ItemDTO getItemById(String id) {
            // New method to implement the abstract method from ItemService
            ItemDTO item = new ItemDTO();
            item.setId(id);
            item.setTitle("Found via String ID");
            item.setAuthor("Another Author");
            item.setDescription("Another Description");
            item.setPrice(new BigDecimal("99.99"));
            item.setStockQuantity(5);
            item.setImageUrl("string_id_image.jpg");
            item.setCategoryId("3");
            item.setCategoryName("New Category");
            return item;
        }
        
         @Override
        public List<ItemDTO> getItemsByCategory(String category) {
            // A mock implementation that returns a list of items based on the category
            List<ItemDTO> items = new ArrayList<>();
            if ("Electronics".equals(category)) {
                ItemDTO item1 = new ItemDTO();
                item1.setId("1");
                item1.setTitle("Laptop");
                item1.setCategoryName("Electronics");
                items.add(item1);
                ItemDTO item2 = new ItemDTO();
                item2.setId("2");
                item2.setTitle("Headphones");
                item2.setCategoryName("Electronics");
                items.add(item2);
            }
            return items;
        }
        
        
        @Override
        public List<ItemDTO> getAllItems() {
            // New method to implement the abstract method from ItemService
            return new ArrayList<>();
        }
        
         @Override
        public void addItem(ItemDTO itemDTO) {
            // A mock implementation that does nothing, as it's a void method.
        }
        
    }

    private static class MockCategoryService implements CategoryService {
        
        @Override
        public int getLastCategoryIdNumber() {
            // Mock implementation to return a fixed ID number for testing purposes
            return 10;
        }
        
        @Override
        public boolean isNameExists(String name) {
            // Mock implementation to satisfy the abstract method
            return false;
        }
        
         @Override
        public void deleteCategory(String id) {
            // A mock implementation that does nothing since it's a void method.
        }
        
        @Override
        public void updateCategory(CategoryDTO categoryDTO) {
            // A mock implementation that does nothing, as it's a void method.
        }
        
         @Override
        public CategoryDTO getCategoryById(String id) {
            // A mock implementation that returns a dummy DTO
            CategoryDTO category = new CategoryDTO();
            category.setId(id);
            category.setName("Mock Category");
            return category;
        }
         @Override
        public List<CategoryDTO> getAllCategories() {
            List<CategoryDTO> categories = new ArrayList<>();
            categories.add(new CategoryDTO("1", "Electronics", "image1.jpg", new Timestamp(System.currentTimeMillis())));
            categories.add(new CategoryDTO("2", "Clothing", "image2.jpg", new Timestamp(System.currentTimeMillis())));
            categories.add(new CategoryDTO("3", "Books", "image3.jpg", new Timestamp(System.currentTimeMillis())));
            return categories;
        } 
        
         @Override
        public void addCategory(CategoryDTO categoryDTO) {
            // A mock implementation that does nothing, since it's a void method.
        }
       
    }
}