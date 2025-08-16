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
    private final IDGenerator<String> orderIdGenerator; // New dependency

    public OrderPreparationService(OrderDAO orderDAO, OrderItemDAO orderItemDAO, IDGenerator<String> orderIdGenerator) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.orderIdGenerator = orderIdGenerator;
    }

    public OrderDTO prepareOrder(String userId, String fullName, String email, String shippingAddress, String paymentMethod, Map<String, CartItem> cart, BigDecimal deliveryFare)
            throws Exception {

        String orderId = orderIdGenerator.generate();

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
        order.setCustomerName(fullName);
        order.setEmail(email);
        order.setShippingAddress(shippingAddress);
        order.setOrderDate(new Date());
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(total);
        order.setDeliveryFare(deliveryFare);
        order.setItems(orderItems);
        order.setStatus("PENDING");

        return order;
    }
}