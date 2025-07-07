package service.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;
import util.EmailSender;

import util.*;
import java.util.List;

public class OrderEmailServiceImpl implements OrderEmailService {

    private final EmailSender emailSender;

    public OrderEmailServiceImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

  @Override
public void sendOrderConfirmation(String email, OrderDTO order, List<OrderItemDTO> items, String invoiceHtml, byte[] pdfBytes, String pdfFileName) throws Exception {
    String subject = "Order Confirmation - Order #" + order.getOrderId();
    emailSender.sendEmailWithAttachment(email, subject, invoiceHtml, pdfBytes, pdfFileName);
}


}
