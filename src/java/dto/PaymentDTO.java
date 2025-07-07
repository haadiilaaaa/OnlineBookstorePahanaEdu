package dto;

public class PaymentDTO {
    private String paymentId;
    private String orderId;
    private String method;
    private String cardNumberMasked; // e.g. **** **** **** 1234
    private String status;

    public String getPaymentId() {
        return paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMethod() {
        return method;
    }

    public String getCardNumberMasked() {
        return cardNumberMasked;
    }

    public String getStatus() {
        return status;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setCardNumberMasked(String cardNumberMasked) {
        this.cardNumberMasked = cardNumberMasked;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
