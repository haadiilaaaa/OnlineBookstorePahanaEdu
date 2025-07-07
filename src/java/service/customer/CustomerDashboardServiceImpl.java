package service.customer;
import java.math.BigDecimal;
import dao.CartItemDAO;
import dao.CustomerDAO;
import dao.DiscountDAO;
import dto.CustomerDashboardDTO;
import model.CartItem;
import dao.CategoryDAO;
import dao.CustomerDAO;
import dao.CartItemDAO;
import dao.DiscountDAO;
import dto.CustomerDashboardDTO;
import model.Category;
import model.Customer;
import model.Discount;
import dao.*;
import db.DBConnection;



import java.util.List;
public class CustomerDashboardServiceImpl implements CustomerDashboardService {

    private final CustomerDAO customerDAO;
    private final CartItemDAO cartItemDAO;
    private final DiscountDAO discountDAO;
    private final CategoryDAO categoryDAO;  // You need this DAO injected

    // Update constructor to accept CategoryDAO
    public CustomerDashboardServiceImpl(CustomerDAO customerDAO, CartItemDAO cartItemDAO, DiscountDAO discountDAO, CategoryDAO categoryDAO) {
        this.customerDAO = customerDAO;
        this.cartItemDAO = cartItemDAO;
        this.discountDAO = discountDAO;
        this.categoryDAO = categoryDAO;
    }

@Override
public CustomerDashboardDTO loadDashboard(String customerId) throws Exception {
    System.out.println("🔍 Loading dashboard for customer ID: " + customerId);
    Customer customer = customerDAO.findById(customerId);
    System.out.println("🔍 Customer found: " + (customer != null));
    List<CartItem> cartItems = cartItemDAO.findByCustomerId(customerId);
    System.out.println("🛒 Cart items loaded: " + (cartItems != null ? cartItems.size() : 0));

    // Setup discount service
    CustomerDiscountService discountService = new CustomerDiscountService(
        new DiscountAssignmentDAOImpl(DBConnection.getInstance().getConnection()),
        new DicountDAOimpl(DBConnection.getInstance().getConnection())
    );

    BigDecimal cartTotal = BigDecimal.ZERO;

    for (CartItem item : cartItems) {
        model.Item fullItem = new ItemDAOImpl(DBConnection.getInstance().getConnection()).findById(item.getItemId());

        if (fullItem != null) {
            BigDecimal discountedPrice = discountService.calculateDiscountedPrice(fullItem);

            item.setPrice(discountedPrice);  // Set discounted price to cart item
            cartTotal = cartTotal.add(discountedPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
        }
    }

    List<Discount> discounts = discountDAO.findAll();
    System.out.println("🎁 Discounts loaded: " + (discounts != null ? discounts.size() : 0));

    CustomerDashboardDTO dto = new CustomerDashboardDTO();
    dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
    dto.setEmail(customer.getEmail());
    dto.setContact(customer.getContactNumber());
    dto.setCartItems(cartItems);
    dto.setCartTotal(cartTotal);
    dto.setActiveDiscounts(discounts);
    dto.setCartItemCount(cartItems != null ? cartItems.size() : 0);

    return dto;
}

    @Override
    public List<Category> getAllCategories() throws Exception {
        return categoryDAO.findAll();
    }
}
