package strategy.payment;

import dto.OrderDTO;



import dao.PaymentDAO;
import dto.OrderDTO;
import model.payment;

import java.math.BigDecimal;

public class DebitCardPayment implements PaymentStrategy {

    private final String cardNumber;
    private final String expiryDate;
    private final String cvv;
    private final PaymentDAO paymentDAO;

    public DebitCardPayment(String cardNumber, String expiryDate, String cvv, PaymentDAO paymentDAO) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.paymentDAO = paymentDAO;
    }

    @Override
    public void processPayment(OrderDTO order) throws Exception {
        System.out.println("✅ Processing *debit card* payment for Order ID: " + order.getOrderId());

        // Simulate success (replace with real validation later if needed)

        payment payment = new payment();
        payment.setId("PAY" + System.currentTimeMillis());
        payment.setOrderId(order.getOrderId());
        payment.setMethod("Debit Card");
        payment.setStatus("SUCCESS");
        payment.setAmount(order.getTotalAmount());

        paymentDAO.save(payment);
    }
}
