package service.deliveryPartner;

import dao.OrderDAO;
import dto.OrderDTO;
import util.*;
import java.util.List;

public class DeliveryOrderServiceImpl implements DeliveryOrderService {

    private final OrderDAO orderDAO;
    private final EmailSender emailSender;

    public DeliveryOrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
        this.emailSender = EmailServiceFactory.createGeneralEmailService(); // FIXED
    }
    @Override
    public List<OrderDTO> getOrdersAssignedToPartner(String partnerId) throws Exception {
        return orderDAO.findOrdersByDeliveryPartner(partnerId);
    }

   @Override
public void markAsDelivered(String orderId) throws Exception {
    orderDAO.updateOrderStatus(orderId, "DELIVERED");

    String customerEmail = orderDAO.getCustomerEmailByOrderId(orderId);
    if (customerEmail != null && !customerEmail.isEmpty()) {
        String subject = "Order Delivered: " + orderId;
        String body = "Dear Customer,\n\nYour order with ID " + orderId + " has been delivered successfully.\n\nThank you for shopping with us!";

        emailSender.sendEmail(customerEmail, subject, body);
    }
}

    
    @Override
public String getCustomerEmailByOrderId(String orderId) throws Exception {
    return orderDAO.getCustomerEmailByOrderId(orderId);
}
@Override
public void cancelOrder(String orderId) throws Exception {
    orderDAO.updateOrderStatus(orderId, "CANCELLED");

    String customerEmail = orderDAO.getCustomerEmailByOrderId(orderId);
    if (customerEmail != null && !customerEmail.isEmpty()) {
        String subject = "Order Cancelled: " + orderId;
        String body = "Dear Customer,\n\nYour order with ID " + orderId + " has been cancelled by the delivery partner.\n\nIf this is a mistake, please contact support.";
        emailSender.sendEmail(customerEmail, subject, body);
    }
}

}
