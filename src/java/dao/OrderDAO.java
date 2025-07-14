package dao;

import dto.OrderDTO;
import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    void saveOrder(OrderDTO order) throws Exception;
    int getNextOrderNumber() throws Exception;
    List<OrderDTO> findOrdersByCustomerId(String customerId) throws Exception;
    void updateOrderStatus(String orderId, String status) throws Exception;
   List<OrderDTO> findAllOrdersWithCustomerInfo() throws Exception;
OrderDTO findOrderWithCustomerById(String orderId) throws Exception;
 Optional<OrderDTO> findOrderById(String orderId) throws Exception;





}
