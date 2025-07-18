package dao;

import dto.OrderDTO;
import java.util.List;
import java.util.Optional;
import util.DAOExeption;
import java.math.BigDecimal;

public interface OrderDAO {
    void saveOrder(OrderDTO order) throws Exception;
    int getNextOrderNumber() throws Exception;
    List<OrderDTO> findOrdersByCustomerId(String customerId) throws Exception;
    void updateOrderStatus(String orderId, String status) throws Exception;
   List<OrderDTO> findAllOrdersWithCustomerInfo() throws Exception;
OrderDTO findOrderWithCustomerById(String orderId) throws Exception;
 Optional<OrderDTO> findOrderById(String orderId) throws Exception;
 boolean assignDeliveryPartner(String orderId, String partnerId) throws DAOExeption;

String getCustomerEmailByOrderId(String orderId) throws DAOExeption;
List<OrderDTO> findOrdersByDeliveryPartner(String partnerId) throws Exception;
// In OrderDAO interface
BigDecimal getTotalEarningsByDeliveryPartner(String partnerId) throws DAOExeption;
// In OrderDAO interface
int getTotalDeliveriesByDeliveryPartner(String partnerId) throws Exception;
int countOrdersByPartnerAndStatus(String partnerId, String status) throws Exception;

List<OrderDTO> findOrdersByDeliveryPartnerWithStatus(String partnerId, String status) throws Exception;










}
