package service.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;

import java.util.List;

public interface OrderEmailService {
    void sendOrderConfirmation(
        String email,
        OrderDTO order,
        List<OrderItemDTO> items,
        String invoiceHtml,
        byte[] pdfBytes,
        String pdfFileName
    ) throws Exception;
}
