package strategy.payment;

import dto.OrderDTO;
import dao.*;
import model.payment;

public class CreditCardPayment implements PaymentStrategy {

    private final String cardNumber;
    private final String expiryDate;
    private final String cvv;
    private final PaymentDAO paymentDAO;

    public CreditCardPayment(String cardNumber, String expiryDate, String cvv, PaymentDAO paymentDAO) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.paymentDAO = paymentDAO;
    }

    @Override
    public void processPayment(OrderDTO order) throws Exception {
        // Simulate payment success
        System.out.println("✅ Credit card processed.");

        payment payment = new payment();
        payment.setId("PAY" + System.currentTimeMillis());
        payment.setOrderId(order.getOrderId());
        payment.setMethod("Credit Card");
        payment.setStatus("SUCCESS");
        payment.setAmount(order.getTotalAmount());

        paymentDAO.save(payment);
    }
}

