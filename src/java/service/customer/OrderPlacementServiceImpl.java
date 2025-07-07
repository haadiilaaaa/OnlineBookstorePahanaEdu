package service.customer;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dto.OrderDTO;
import dto.OrderItemDTO;
import util.EmailSender;
import service.customer.InvoiceService;
import service.customer.OrderPlacementService;
import util.*;
import java.sql.Connection;
import java.util.List;

public class OrderPlacementServiceImpl implements OrderPlacementService {

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final InvoiceService invoiceService;
    private final OrderEmailService orderEmailService;

    public OrderPlacementServiceImpl(OrderDAO orderDAO,
                            OrderItemDAO orderItemDAO,
                            InvoiceService invoiceService,
                            OrderEmailService orderEmailService) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.invoiceService = invoiceService;
        this.orderEmailService = orderEmailService;
    }

   @Override
public void placeOrder(OrderDTO order, List<OrderItemDTO> items, String customerEmail, String invoiceHtml, byte[] pdfBytes) throws Exception {
    order.setStatus("PENDING"); // Ensure all new orders get default status

    orderDAO.saveOrder(order);
    orderItemDAO.saveOrderItems(items);

    // Send confirmation email with invoice PDF attached
    orderEmailService.sendOrderConfirmation(customerEmail, order, items, invoiceHtml, pdfBytes, "Invoice_" + order.getOrderId() + ".pdf");
}


    // Getter for OrderDAO if needed
    public OrderDAO getOrderDAO() {
        return this.orderDAO;
    }
}
