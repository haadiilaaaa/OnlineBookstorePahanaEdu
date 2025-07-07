package dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private String orderItemId;
    private String orderId;
    private String itemId;
    private String itemTitle; // Optional – helpful in invoice or summary
    private int quantity;
    private BigDecimal price;

    public String getOrderItemId() {
        return orderItemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    
}
