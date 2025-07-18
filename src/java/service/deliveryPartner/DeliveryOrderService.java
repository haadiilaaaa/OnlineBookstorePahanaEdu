package service.deliveryPartner;

import dto.OrderDTO;
import java.util.List;

public interface DeliveryOrderService {
    List<OrderDTO> getOrdersAssignedToPartner(String partnerId) throws Exception;
    void markAsDelivered(String orderId) throws Exception;
     String getCustomerEmailByOrderId(String orderId) throws Exception;
     void cancelOrder(String orderId) throws Exception;
}
