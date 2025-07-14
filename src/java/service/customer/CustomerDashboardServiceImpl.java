package service.customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import dao.*;
import dto.CustomerDashboardDTO;
import model.*;

public class CustomerDashboardServiceImpl implements CustomerDashboardService {

    private final CustomerDAO customerDAO;
    private final CartItemDAO cartItemDAO;
    private final DiscountDAO discountDAO;
    private final CategoryDAO categoryDAO;
    private final CustomerDiscountService discountService;
    private final ItemDAO itemDAO;

    public CustomerDashboardServiceImpl(CustomerDAO customerDAO, CartItemDAO cartItemDAO, DiscountDAO discountDAO,
                                        CategoryDAO categoryDAO, CustomerDiscountService discountService,
                                        ItemDAO itemDAO) {
        this.customerDAO = customerDAO;
        this.cartItemDAO = cartItemDAO;
        this.discountDAO = discountDAO;
        this.categoryDAO = categoryDAO;
        this.discountService = discountService;
        this.itemDAO = itemDAO;
    }

    @Override
    public CustomerDashboardDTO loadDashboard(String customerId) throws Exception {
        Optional<Customer> customerOpt = customerDAO.findById(customerId);
        Customer customer = customerOpt.orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        List<CartItem> cartItems = cartItemDAO.findByCustomerId(customerId);
        BigDecimal cartTotal = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
           Item item = itemDAO.findById(cartItem.getItemId());
            if (item != null) {
                BigDecimal discountedPrice = discountService.calculateDiscountedPrice(item);
                cartItem.setPrice(discountedPrice);
                cartItem.setOriginalPrice(item.getPrice());
                cartTotal = cartTotal.add(discountedPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            }
        }

        List<Category> categories = categoryDAO.findAll();

        CustomerDashboardDTO dto = new CustomerDashboardDTO();
        dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setContact(customer.getContactNumber());
        dto.setCartItems(cartItems);
        dto.setCartTotal(cartTotal);
        dto.setActiveDiscounts(discountDAO.findAll());
        dto.setCartItemCount(cartItems.size());

        return dto;
    }

    @Override
    public List<Category> getAllCategories() throws Exception {
        return categoryDAO.findAll();
    }
}
