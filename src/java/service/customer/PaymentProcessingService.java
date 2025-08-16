package service.customer;

import dao.CartItemDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dto.OrderDTO;
import model.CartItem;
import strategy.payment.PaymentStrategy;

import java.util.Map;

public class PaymentProcessingService {

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final CartItemDAO cartItemDAO;
    private final OrderEmailService orderEmailService;

    public PaymentProcessingService(OrderDAO orderDAO,
                                    OrderItemDAO orderItemDAO,
                                    CartItemDAO cartItemDAO,
                                    OrderEmailService orderEmailService) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.cartItemDAO = cartItemDAO;
        this.orderEmailService = orderEmailService;
    }

    public void process(OrderDTO order,
                    PaymentStrategy strategy,
                    String email,
                    Map<String, CartItem> cart,
                    byte[] pdfBytes) throws Exception {

    // 1. Handle payment first
    strategy.processPayment(order);

    // 2. Set the order status based on the payment outcome
    // This is the missing step in your logic.
    String paymentMethod = order.getPaymentMethod();
    if ("Credit Card".equals(paymentMethod) || "Debit Card".equals(paymentMethod)) {
        order.setStatus("PAID");
    } else {
        order.setStatus("PENDING");
    }

    // 3. Persist the order and items with the correct final status
    orderDAO.saveOrder(order);
    orderItemDAO.saveOrderItems(order.getItems());

    // 4. Send confirmation email
    orderEmailService.sendOrderConfirmation(
            email,
            order,
            order.getItems(),
            order.getInvoiceHtml(),
            pdfBytes,
            "Your Order Confirmation – " + order.getOrderId()
    );

    // 5. Clear cart
    cartItemDAO.deleteCartItemsByUserId(order.getUserId());
    cart.clear();
}
}
