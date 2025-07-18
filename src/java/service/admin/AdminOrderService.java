package service.admin;

import dto.OrderDTO;
import java.util.List;
import dto.*;
public interface AdminOrderService {
    List<OrderDTO> getAllOrdersWithCustomerInfo() throws Exception;
    void updateOrderStatusAndNotify(String orderId, String newStatus) throws Exception;
    boolean assignDeliveryPartner(String orderId, String partnerId) throws Exception;

String getCustomerEmailByOrderId(String orderId) throws Exception;
List<DeliveryPartnerDTO> getAllDeliveryPartners() throws Exception;


}
//