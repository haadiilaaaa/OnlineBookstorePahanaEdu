package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L; // Optional but recommended

    private String id;           // cart_item ID (for DB)
    private String customerId;
    private String itemId;
    private String itemTitle;
    private BigDecimal price;
    private int quantity;
    private String imageUrl;     // ✅ For displaying book cover
    private BigDecimal subtotal; // ✅ For showing subtotal in checkout

    // Constructor for session use (not DB)
    public CartItem(String itemId, String itemTitle, BigDecimal price, int quantity) {
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
    }

    // Full constructor for loading from DB
    public CartItem(String id, String customerId, String itemId, int quantity) {
        this.id = id;
        this.customerId = customerId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    // --- Getters and Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = (this.price != null)
            ? this.price.multiply(BigDecimal.valueOf(quantity))
            : BigDecimal.ZERO;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    private BigDecimal originalPrice;

public BigDecimal getOriginalPrice() {
    return originalPrice;
}

public void setOriginalPrice(BigDecimal originalPrice) {
    this.originalPrice = originalPrice;
}

}
