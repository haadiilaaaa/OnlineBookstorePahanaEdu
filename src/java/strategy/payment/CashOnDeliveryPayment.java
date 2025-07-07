package strategy.payment;

import dao.PaymentDAO;
import dto.OrderDTO;
import model.payment;

public class CashOnDeliveryPayment implements PaymentStrategy {

    private final PaymentDAO paymentDAO;

    public CashOnDeliveryPayment(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @Override
    public void processPayment(OrderDTO order) throws Exception {
        System.out.println("✅ Payment will be collected upon delivery for Order ID: " + order.getOrderId());

        payment payment = new payment();
        payment.setId("PAY" + System.currentTimeMillis());
        payment.setOrderId(order.getOrderId());
        payment.setMethod("Cash on Delivery");
        payment.setStatus("PENDING");
        payment.setAmount(order.getTotalAmount());

        paymentDAO.save(payment);
    }
}
