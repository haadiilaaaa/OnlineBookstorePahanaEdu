package service.deliveryPartner;

import dao.DeliveryPartnerDAO;
import dao.OrderDAO;
import dto.DeliveryPartnerDTO;
import mapper.UserMapper;
import model.DeliveryPartner;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Optional;
import dto.*;

public class DeliveryPartnerProfileServiceImpl implements DeliveryPartnerProfileService {

    private final DeliveryPartnerDAO deliveryPartnerDAO;
    private final OrderDAO orderDAO;

    public DeliveryPartnerProfileServiceImpl(DeliveryPartnerDAO deliveryPartnerDAO, OrderDAO orderDAO) {
        this.deliveryPartnerDAO = deliveryPartnerDAO;
        this.orderDAO = orderDAO;
    }

    @Override
    public DeliveryPartnerDTO getProfile(String id) throws Exception {
        Optional<DeliveryPartner> optional = deliveryPartnerDAO.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Delivery Partner not found");
        }
        DeliveryPartner dp = optional.get();
       return UserMapper.toDeliveryPartnerDTO(dp);

    }

    @Override
    public void updateProfile(DeliveryPartnerDTO dto) throws Exception {
        // Optionally validate fields here

        Optional<DeliveryPartner> optional = deliveryPartnerDAO.findById(dto.getId());
        if (optional.isEmpty()) {
            throw new Exception("Delivery Partner not found");
        }
        DeliveryPartner existing = optional.get();

        // Map updated fields from DTO to existing model
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setContactNumber(dto.getContactNumber());
        existing.setVehicleNumber(dto.getVehicleNumber());
        // optionally update username if allowed

       deliveryPartnerDAO.update(existing); // ✅ correct
 // Or create an update method in DAO for partial update
    }

    @Override
    public BigDecimal getTotalEarnings(String partnerId) throws Exception {
        return orderDAO.getTotalEarningsByDeliveryPartner(partnerId);
    }
    
    @Override
public int getTotalDeliveries(String partnerId) throws Exception {
    return orderDAO.getTotalDeliveriesByDeliveryPartner(partnerId);
}
@Override
public int getPendingDeliveries(String partnerId) throws Exception {
    return orderDAO.countOrdersByPartnerAndStatus(partnerId, "ASSIGNED");
}

@Override
public List<String> getNotifications(String partnerId) throws Exception {
    // For demo: fetch latest assigned orders and return messages like "New order XYZ assigned"
    List<OrderDTO> assignedOrders = orderDAO.findOrdersByDeliveryPartnerWithStatus(partnerId, "ASSIGNED");
    List<String> notifications = new ArrayList<>();
    for (OrderDTO order : assignedOrders) {
        notifications.add("New order assigned: " + order.getOrderId());
    }
    return notifications;
}

}
