package service.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;

import java.math.BigDecimal;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {

  @Override
public String generateInvoice(OrderDTO order, List<OrderItemDTO> items) {
    StringBuilder sb = new StringBuilder();

    sb.append("<h2>Invoice - Order ID: ").append(order.getOrderId()).append("</h2>");
    sb.append("<p><strong>Date:</strong> ").append(order.getOrderDate()).append("</p>");
    sb.append("<p><strong>Customer:</strong> ").append(order.getCustomerName()).append("</p>");
    sb.append("<p><strong>Email:</strong> ").append(order.getEmail()).append("</p>");
    sb.append("<p><strong>Shipping Address:</strong> ").append(order.getShippingAddress()).append("</p>");

    sb.append("<hr><table border='1' cellpadding='5' cellspacing='0' style='width:100%; border-collapse: collapse;'>");
    sb.append("<tr><th>Title</th><th>Price</th><th>Qty</th><th>Subtotal</th></tr>");

    BigDecimal itemsTotal = BigDecimal.ZERO;
    for (OrderItemDTO item : items) {
        BigDecimal subtotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
        itemsTotal = itemsTotal.add(subtotal);

        sb.append("<tr>");
        sb.append("<td>").append(item.getItemTitle()).append("</td>");
        sb.append("<td>").append(item.getPrice()).append("</td>");
        sb.append("<td>").append(item.getQuantity()).append("</td>");
        sb.append("<td>").append(subtotal).append("</td>");
        sb.append("</tr>");
    }

    // Add delivery fare row
    BigDecimal deliveryFare = order.getDeliveryFare() != null ? order.getDeliveryFare() : BigDecimal.ZERO;
    sb.append("<tr>");
    sb.append("<td colspan='3' style='text-align:right;'><strong>Delivery Fare</strong></td>");
    sb.append("<td>").append(deliveryFare).append("</td>");
    sb.append("</tr>");

    // Total including delivery fare
    BigDecimal grandTotal = itemsTotal.add(deliveryFare);
    sb.append("<tr><td colspan='3' style='text-align:right;'><strong>Total</strong></td><td><strong>")
      .append(grandTotal).append("</strong></td></tr>");

    sb.append("</table><hr>");
    sb.append("<p>Thank you for your order!</p>");

    return sb.toString();
}

}