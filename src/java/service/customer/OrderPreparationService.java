package service.customer;

import dao.OrderDAO;
import dto.OrderDTO;
import dto.OrderItemDTO;
import model.CartItem;
import util.IDGenerator;
import dao.*;
import java.math.BigDecimal;
import java.util.*;

public class OrderPreparationService {

   private final OrderDAO orderDAO;
private final OrderItemDAO orderItemDAO;

public OrderPreparationService(OrderDAO orderDAO, OrderItemDAO orderItemDAO) {
    this.orderDAO = orderDAO;
    this.orderItemDAO = orderItemDAO;
}

public OrderDTO prepareOrder(String userId, String fullName, String email, String shippingAddress, String paymentMethod, Map<String, CartItem> cart, BigDecimal deliveryFare)
 throws Exception {

    int nextOrderNumber = orderDAO.getNextOrderNumber();
    String orderId = IDGenerator.generateOrderId(nextOrderNumber);

    int nextOrderItemNumber = orderItemDAO.getNextOrderItemNumber();

    BigDecimal total = BigDecimal.ZERO;
    List<OrderItemDTO> orderItems = new ArrayList<>();

    int counter = 0;

    for (CartItem item : cart.values()) {
        String orderItemId = "OID" + String.format("%03d", nextOrderItemNumber + counter++);
        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setOrderItemId(orderItemId);
        orderItem.setOrderId(orderId);
        orderItem.setItemId(item.getItemId());
        orderItem.setItemTitle(item.getItemTitle());
        orderItem.setPrice(item.getPrice());
        orderItem.setQuantity(item.getQuantity());

        BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        total = total.add(subtotal);
        orderItems.add(orderItem);
    }

    // Add delivery fare to total
    total = total.add(deliveryFare);

    OrderDTO order = new OrderDTO();
    order.setOrderId(orderId);
    order.setUserId(userId);
    order.setCustomerName(fullName);  // fixed from customerName to fullName
    order.setEmail(email);
    order.setShippingAddress(shippingAddress);
    order.setOrderDate(new Date());
    order.setPaymentMethod(paymentMethod);
    order.setTotalAmount(total);
    order.setDeliveryFare(deliveryFare); // set delivery fare here
    order.setItems(orderItems);
    order.setStatus("PENDING");

    return order;
}

}