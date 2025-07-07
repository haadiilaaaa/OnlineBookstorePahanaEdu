package service.customer;

import dto.OrderDTO;
import java.util.List;

public interface CustomerOrderHistoryService {
    List<OrderDTO> getOrdersByCustomer(String customerId) throws Exception;
}
