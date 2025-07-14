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

        // Persist order and items
        orderDAO.saveOrder(order);
        orderItemDAO.saveOrderItems(order.getItems());

        // Handle payment
        strategy.processPayment(order);

        // Send confirmation email
        orderEmailService.sendOrderConfirmation(
                email,
                order,
                order.getItems(),
                order.getInvoiceHtml(),
                pdfBytes,
                "Your Order Confirmation – " + order.getOrderId()
        );

        // Clear cart in DB and memory
        cartItemDAO.deleteCartItemsByUserId(order.getUserId());
        cart.clear();
    }
}
