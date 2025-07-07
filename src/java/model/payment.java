package model;

import java.math.BigDecimal;
import java.util.Date;

public class payment {
    private String id;
    private String orderId;
    private String method;
    private String status;
    private BigDecimal amount;
    private Date createdAt;

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMethod() {
        return method;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    
}
