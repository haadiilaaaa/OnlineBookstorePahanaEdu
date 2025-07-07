package service.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;
import java.util.List;

public interface InvoiceService {
    String generateInvoice(OrderDTO order, List<OrderItemDTO> items);
}
