package service.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;

import java.math.BigDecimal;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public String generateInvoice(OrderDTO order, List<OrderItemDTO> items) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><body>");
        sb.append("<h2>Invoice - Order ID: ").append(order.getOrderId()).append("</h2>");
        sb.append("<p><strong>Date:</strong> ").append(order.getOrderDate()).append("</p>");
        sb.append("<p><strong>Customer:</strong> ").append(order.getCustomerName()).append("</p>");
        sb.append("<p><strong>Email:</strong> ").append(order.getEmail()).append("</p>");
        sb.append("<p><strong>Shipping Address:</strong> ").append(order.getShippingAddress()).append("</p>");

        sb.append("<hr><table border='1' cellpadding='5' cellspacing='0'>");
        sb.append("<tr><th>Title</th><th>Price</th><th>Qty</th><th>Subtotal</th></tr>");

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemDTO item : items) {
            BigDecimal subtotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            total = total.add(subtotal);

            sb.append("<tr>");
            sb.append("<td>").append(item.getItemTitle()).append("</td>");
            sb.append("<td>").append(item.getPrice()).append("</td>");
            sb.append("<td>").append(item.getQuantity()).append("</td>");
            sb.append("<td>").append(subtotal).append("</td>");
            sb.append("</tr>");
        }

        sb.append("<tr><td colspan='3'><strong>Total</strong></td><td><strong>").append(total).append("</strong></td></tr>");
        sb.append("</table><hr>");

        sb.append("<p>Thank you for your order!</p>");
        sb.append("</body></html>");

        return sb.toString();
    }
}
