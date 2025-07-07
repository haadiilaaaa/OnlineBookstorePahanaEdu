package service.admin;

import dto.OrderDTO;
import java.util.List;

public interface AdminOrderService {
    List<OrderDTO> getAllOrdersWithCustomerInfo() throws Exception;
    void updateOrderStatusAndNotify(String orderId, String newStatus) throws Exception;
}
