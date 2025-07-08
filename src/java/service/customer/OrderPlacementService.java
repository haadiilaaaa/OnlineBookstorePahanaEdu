package service.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;

import java.util.List;

public interface OrderPlacementService {
  void placeOrder(OrderDTO order, List<OrderItemDTO> items, String customerEmail, String invoiceHtml, byte[] pdfBytes) throws Exception;


}
//hii