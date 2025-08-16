package customer;

import controller.customer.OrderConfirmationServlet;
import dao.*;
import dto.OrderDTO;
import dto.OrderItemDTO;
import dto.UserSession;
import mockhttp.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.customer.InvoiceService;
import service.customer.OrderInvoiceHelper;
import model.Item;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import static org.junit.Assert.*;
import static util.contannts.PagePaths.CUSTOMER_DASHBOARD;
import static util.contannts.PagePaths.THANK_YOU_PAGE;
import static util.contannts.SessionKeys.ORDER_ID;

public class OrderConfirmationServletTest {

    private OrderConfirmationServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;
    private MockServletContext servletContext;

    private MockOrderDAO mockOrderDAO;
    private MockOrderItemDAO mockOrderItemDAO;
    private MockInvoiceService mockInvoiceService;
    private MockItemDAO mockItemDAO;
    private MockOrderInvoiceHelper mockOrderInvoiceHelper;

    private UserSession testUserSession;
    private static final String TEST_ORDER_ID = "test-order-123";

    @Before
    public void setUp() throws ServletException {
        servlet = new OrderConfirmationServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        servletContext = new MockServletContext();

        mockOrderDAO = new MockOrderDAO();
        mockOrderItemDAO = new MockOrderItemDAO();
        mockInvoiceService = new MockInvoiceService();
        mockItemDAO = new MockItemDAO();
        mockOrderInvoiceHelper = new MockOrderInvoiceHelper();

        servletContext.setAttribute("OrderDAO", mockOrderDAO);
        servletContext.setAttribute("OrderItemDAO", mockOrderItemDAO);
        servletContext.setAttribute("InvoiceService", mockInvoiceService);
        servletContext.setAttribute("ItemDAO", mockItemDAO);
        servletContext.setAttribute("OrderInvoiceHelper", mockOrderInvoiceHelper);

        testUserSession = new UserSession("cus001", "testuser", "test@example.com", "customer", "Test", "User", "123 Test St", "1234567890");
        session.setAttribute("user", testUserSession);
        request.setSession(session);

        servlet.init(new MockServletConfig(servletContext));
    }

    @After
    public void tearDown() {
        mockOrderDAO.reset();
        mockOrderItemDAO.reset();
    }
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testDoGet_Success() throws ServletException, IOException {
        // Arrange
        String userId = "cus001";
        String mockItemId = "item001";
        BigDecimal itemPrice = new BigDecimal("1200.00");

        session.setAttribute(ORDER_ID, TEST_ORDER_ID);

        Item mockItem = new Item();
        mockItem.setId(mockItemId);
        mockItem.setTitle("Test Item Title");
        mockItem.setPrice(itemPrice);

        OrderItemDTO mockOrderItem = new OrderItemDTO();
        mockOrderItem.setItemId(mockItemId);
        mockOrderItem.setQuantity(1);
        mockOrderItem.setPrice(itemPrice);
        List<OrderItemDTO> mockItems = Collections.singletonList(mockOrderItem);

        OrderDTO mockOrder = new OrderDTO();
        mockOrder.setOrderId(TEST_ORDER_ID);
        mockOrder.setUserId(userId);
        mockOrder.setShippingAddress("Test Address");
        mockOrder.setPaymentMethod("Credit Card");
        mockOrder.setOrderDate(new Date());
        mockOrder.setStatus("PAID");
        mockOrder.setDeliveryFare(new BigDecimal("5.00"));

        // Crucial step: Set the items on the order mock.
        mockOrder.setItems(mockItems);

        // Mock DAO and Item data
        mockOrderDAO.setOrder(Optional.of(mockOrder));
        mockOrderItemDAO.setItems(mockItems);
        mockItemDAO.setItem(mockItem);

        // Act
        servlet.service(request, response);

        // Assert
        assertEquals(THANK_YOU_PAGE, response.getForwardedUrl());
        assertNull(session.getAttribute(ORDER_ID));
        assertNotNull(request.getAttribute("order"));
        assertNotNull(request.getAttribute("invoiceData"));
    }
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testDoGet_NoOrderIdInSession_Redirects() throws ServletException, IOException {
        servlet.service(request, response);
        String expectedUrl = request.getContextPath() + CUSTOMER_DASHBOARD;
        assertEquals(expectedUrl, response.getRedirectedUrl());
    }
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testDoGet_OrderNotFound_Redirects() throws ServletException, IOException {
        session.setAttribute(ORDER_ID, TEST_ORDER_ID);
        mockOrderDAO.setOrder(Optional.empty());
        servlet.service(request, response);
        assertEquals(request.getContextPath() + CUSTOMER_DASHBOARD, response.getRedirectedUrl());
    }
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testDoGet_UserIdMismatch_Redirects() throws ServletException, IOException {
        session.setAttribute(ORDER_ID, TEST_ORDER_ID);
        
        OrderDTO mockOrder = new OrderDTO();
        mockOrder.setOrderId(TEST_ORDER_ID);
        mockOrder.setUserId("otherUser002");
        mockOrder.setTotalAmount(new BigDecimal("100.00"));
        mockOrder.setShippingAddress("Test Address");
        mockOrder.setPaymentMethod("Credit Card");
        mockOrder.setOrderDate(new Date());
        mockOrder.setStatus("PAID");
        mockOrder.setDeliveryPartnerId(null);
        mockOrder.setDeliveryFare(null);

        List<OrderItemDTO> mockItems = Collections.singletonList(new OrderItemDTO());
        mockOrderDAO.setOrder(Optional.of(mockOrder));
        mockOrderItemDAO.setItems(mockItems);
        mockItemDAO.setItem(new Item());

        servlet.service(request, response);
        assertEquals(request.getContextPath() + CUSTOMER_DASHBOARD, response.getRedirectedUrl());
    }
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    class MockOrderDAO implements OrderDAO {
        private Optional<OrderDTO> order;
        public void setOrder(Optional<OrderDTO> order) { this.order = order; }
        public void reset() { this.order = null; }
        @Override public Optional<OrderDTO> findOrderById(String id) throws Exception { return this.order; }
        @Override public void saveOrder(OrderDTO order) { /* Mock */ }
        @Override public void deleteOrdersByUserId(String userId) { /* Mock */ }
        @Override public List<OrderDTO> findAll() { return Collections.emptyList(); }
        @Override public List<OrderDTO> findOrdersByDeliveryPartnerWithStatus(String deliveryPartnerId, String status) { return Collections.emptyList(); }
        @Override public int countOrdersByPartnerAndStatus(String deliveryPartnerId, String status) { return 0; }
        @Override public int getTotalDeliveriesByDeliveryPartner(String deliveryPartnerId) { return 0; }
        @Override public BigDecimal getTotalEarningsByDeliveryPartner(String deliveryPartnerId) { return BigDecimal.ZERO; }
        @Override public List<OrderDTO> findOrdersByDeliveryPartner(String deliveryPartnerId) { return Collections.emptyList(); }
        @Override public String getCustomerEmailByOrderId(String orderId) { return "test@example.com"; }
        @Override public boolean assignDeliveryPartner(String orderId, String deliveryPartnerId) { return true; }
        @Override public OrderDTO findOrderWithCustomerById(String orderId) { return null; }
        @Override public List<OrderDTO> findAllOrdersWithCustomerInfo() { return Collections.emptyList(); }
        @Override public void updateOrderStatus(String orderId, String newStatus) { /* Mock */ }
        @Override public List<OrderDTO> findOrdersByCustomerId(String customerId) { return Collections.emptyList(); }
        @Override public int getNextOrderNumber() { return 1; }
    }

    class MockOrderItemDAO implements OrderItemDAO {
        private List<OrderItemDTO> items;
        public void setItems(List<OrderItemDTO> items) { this.items = items; }
        public void reset() { this.items = null; }
        @Override public List<OrderItemDTO> findItemsByOrderId(String orderId) { return items; }
        @Override public void deleteOrderItemsByUserId(String userId) { /* Mock */ }
        @Override public void saveOrderItems(List<OrderItemDTO> items) { /* Mock */ }
        @Override public int getNextOrderItemNumber() { return 1; }
    }

    class MockInvoiceService implements InvoiceService {
        @Override public String generateInvoice(OrderDTO order, List<OrderItemDTO> items) { return "<html><body><h1>Sample Invoice for Order " + order.getOrderId() + "</h1></body></html>"; }
    }

    class MockItemDAO implements ItemDAO {
        private Item mockItem;
        public void setItem(Item item) { this.mockItem = item; }
        @Override public List<Item> advancedSearch(String itemName, String category, BigDecimal minPrice, BigDecimal maxPrice) { return Collections.emptyList(); }
        @Override public List<Item> findByCategoryId(String categoryId) { return Collections.emptyList(); }
        @Override public void delete(String id) { /* Mock */ }
        @Override public void update(Item item) { /* Mock */ }
        @Override public int getItemCount() { return 0; }
        @Override public List<Item> findAll() { return Collections.emptyList(); }
        @Override public void save(Item item) { /* Mock */ }
        @Override public Item findById(String id) { return this.mockItem; }
    }
    
    // The mock helper now correctly sets all required request attributes.
    class MockOrderInvoiceHelper {
        public void prepareInvoiceData(HttpServletRequest req, OrderDTO order, UserSession user, List<OrderItemDTO> items, InvoiceService invoiceService, ItemDAO itemDAO, BigDecimal deliveryFare) throws Exception {
            order.setCustomerName(user.getFullName());
            order.setEmail(user.getEmail());
            order.setShippingAddress(user.getAddress());
            order.setItems(items);
            order.setDeliveryFare(deliveryFare);
            BigDecimal itemsTotal = items.stream().map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalAmount(itemsTotal.add(deliveryFare));
            String invoiceHtml = invoiceService.generateInvoice(order, items);
            order.setInvoiceHtml(invoiceHtml);
            order.setInvoiceDownloadPath("invoices/Invoice_" + order.getOrderId() + ".pdf");
            
            req.setAttribute("order", order);
            req.setAttribute("invoice", invoiceHtml);
            req.setAttribute("invoiceDownloadPath", order.getInvoiceDownloadPath());
        }
    }
}