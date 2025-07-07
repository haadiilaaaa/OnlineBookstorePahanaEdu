package dto;

import java.math.BigDecimal;
import java.util.List;
import model.CartItem;
import model.Discount;

public class CustomerDashboardDTO {
    private String customerName;
    private String email;
    private String contact;
    private BigDecimal cartTotal;
    private List<CartItem> cartItems;
    private List<Discount> activeDiscounts;
    private int cartItemCount;  // <-- add this

    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<Discount> getActiveDiscounts() {
        return activeDiscounts;
    }
    public void setActiveDiscounts(List<Discount> activeDiscounts) {
        this.activeDiscounts = activeDiscounts;
    }

    public BigDecimal getCartTotal() {
        return cartTotal;
    }
    public void setCartTotal(BigDecimal cartTotal) {
        this.cartTotal = cartTotal;
    }

    // New getter and setter for cartItemCount
    public int getCartItemCount() {
        return cartItemCount;
    }
    public void setCartItemCount(int cartItemCount) {
        this.cartItemCount = cartItemCount;
    }
}
