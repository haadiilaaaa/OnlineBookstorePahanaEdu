package dao;

import dto.OrderItemDTO;

import java.util.List;
import java.sql.SQLException;

public interface OrderItemDAO {
      int getNextOrderItemNumber() throws SQLException;
    void saveOrderItems(List<OrderItemDTO> items) throws Exception;
  

}
