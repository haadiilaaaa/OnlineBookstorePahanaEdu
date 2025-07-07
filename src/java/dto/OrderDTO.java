package dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class OrderDTO {
    private String orderId;
    private String userId;
    private String shippingAddress;
    private Date orderDate;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private List<OrderItemDTO> items;
    private String customerName;
    private String email;

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
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
private String status;

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}


    
}
