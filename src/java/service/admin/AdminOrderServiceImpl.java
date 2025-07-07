package service.admin;

import dao.OrderDAO;
import dto.OrderDTO;
import util.EmailSender;
import util.GeneralEmailSenderImpl;
import java.util.List;
 import util.EmailServiceFactory;

public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderDAO orderDAO;
    private final EmailSender emailSender;

 

public AdminOrderServiceImpl(OrderDAO orderDAO) {
    this.orderDAO = orderDAO;
    this.emailSender = EmailServiceFactory.createGeneralEmailService();
}

    @Override
    public List<OrderDTO> getAllOrdersWithCustomerInfo() throws Exception {
        return orderDAO.findAllOrdersWithCustomerInfo();  // you implement this in DAO
    }

    @Override
    public void updateOrderStatusAndNotify(String orderId, String newStatus) throws Exception {
        // Update status
        orderDAO.updateOrderStatus(orderId, newStatus);

        // Fetch order details for email
        OrderDTO order = orderDAO.findOrderWithCustomerById(orderId);

        // Compose email
        String subject = "Order " + orderId + " status updated to " + newStatus;
        String body = "Dear " + order.getCustomerName() + ",\n\n" +
                "Your order with ID " + orderId + " has been marked as " + newStatus + ".\n\n" +
                "Thank you for shopping with us!\n";

        emailSender.sendEmail(order.getEmail(), subject, body);
    }
}
