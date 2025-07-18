package service.admin;

import dao.OrderDAO;
import dto.OrderDTO;
import util.EmailSender;
import util.GeneralEmailSenderImpl;
import java.util.List;
 import util.EmailServiceFactory;
import dto.*;
import dao.*;
import model.*;
import mapper.*;

public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderDAO orderDAO;
    private final EmailSender emailSender;
    private final DeliveryPartnerDAO deliveryPartnerDAO;


 

public AdminOrderServiceImpl(OrderDAO orderDAO, DeliveryPartnerDAO deliveryPartnerDAO) {
    this.orderDAO = orderDAO;
    this.deliveryPartnerDAO = deliveryPartnerDAO;
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
  @Override
public boolean assignDeliveryPartner(String orderId, String partnerId) throws Exception {
    boolean success = orderDAO.assignDeliveryPartner(orderId, partnerId);
    if (success) {
        // Fetch order and partner info
        OrderDTO order = orderDAO.findOrderWithCustomerById(orderId);
        DeliveryPartner partnerModel = deliveryPartnerDAO.findById(partnerId)
            .orElseThrow(() -> new Exception("Delivery partner not found"));

        DeliveryPartnerDTO partner = DeliveryPartnerMapper.toDTO(partnerModel);

        // Compose email
        String subject = "Delivery Partner Assigned for Your Order";
        String body = "Dear " + order.getCustomerName() + ",\n\n" +
                "Your order (ID: " + orderId + ") has been assigned to our delivery partner:\n" +
                partner.getFirstName() + " " + partner.getLastName() + " (" + partner.getUsername() + ").\n\n" +
                "They will contact you soon to complete the delivery.\n\n" +
                "Thank you for choosing our bookstore!";

        // Send email
        emailSender.sendEmail(order.getEmail(), subject, body);
    }
    return success;
}

@Override
public String getCustomerEmailByOrderId(String orderId) throws Exception {
    return orderDAO.getCustomerEmailByOrderId(orderId);
}
@Override
public List<DeliveryPartnerDTO> getAllDeliveryPartners() throws Exception {
    return deliveryPartnerDAO.getAllPartners();
}

}
