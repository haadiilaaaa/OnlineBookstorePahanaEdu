package dao;

import dto.OrderItemDTO;

import java.util.List;
import java.sql.SQLException;
import util.DAOExeption;

public interface OrderItemDAO {
     List<OrderItemDTO> findItemsByOrderId(String orderId) throws DAOExeption;
int getNextOrderItemNumber() throws DAOExeption;
void saveOrderItems(List<OrderItemDTO> items) throws DAOExeption;
void deleteOrderItemsByUserId(String userId) throws DAOExeption;


     
     
  

}
