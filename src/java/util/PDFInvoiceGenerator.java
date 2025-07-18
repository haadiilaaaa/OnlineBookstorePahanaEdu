// ===========================
// 1. PDFInvoiceGenerator.java
// ===========================
package util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import dto.OrderDTO;
import dto.OrderItemDTO;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

public class PDFInvoiceGenerator {
    public static byte[] generateInvoicePDF(OrderDTO order, List<OrderItemDTO> items) throws Exception {
    Document document = new Document();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    PdfWriter.getInstance(document, baos);
    document.open();

    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    Paragraph title = new Paragraph("Invoice - Order ID: " + order.getOrderId(), titleFont);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);

    document.add(new Paragraph("Date: " + order.getOrderDate()));
    document.add(new Paragraph("Customer: " + order.getCustomerName()));
    document.add(new Paragraph("Email: " + order.getEmail()));
    document.add(new Paragraph("Shipping Address: " + order.getShippingAddress()));
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    Paragraph paymentPara = new Paragraph("Payment Method: " + order.getPaymentMethod(), boldFont);
    document.add(paymentPara);

    document.add(new Paragraph(" "));

    PdfPTable table = new PdfPTable(4);
    table.setWidthPercentage(100);
    table.setWidths(new float[]{4, 2, 1, 2});

    table.addCell("Title");
    table.addCell("Price");
    table.addCell("Qty");
    table.addCell("Subtotal");

    BigDecimal itemsTotal = BigDecimal.ZERO;

    for (OrderItemDTO item : items) {
        table.addCell(item.getItemTitle());
        table.addCell(item.getPrice().toString());
        table.addCell(String.valueOf(item.getQuantity()));
        BigDecimal subtotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
        table.addCell(subtotal.toString());
        itemsTotal = itemsTotal.add(subtotal);
    }

    // Add delivery fare row
    PdfPCell deliveryFareLabel = new PdfPCell(new Phrase("Delivery Fare"));
    deliveryFareLabel.setColspan(3);
    deliveryFareLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
    table.addCell(deliveryFareLabel);

    PdfPCell deliveryFareValue = new PdfPCell(new Phrase("Rs. " + order.getDeliveryFare().toPlainString()));
    table.addCell(deliveryFareValue);

    // Add total row (items total + delivery fare)
    PdfPCell totalCell = new PdfPCell(new Phrase("Total"));
    totalCell.setColspan(3);
    totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    table.addCell(totalCell);

    PdfPCell totalValue = new PdfPCell(new Phrase("Rs. " + order.getTotalAmount().toPlainString()));
    table.addCell(totalValue);

    document.add(table);

    document.close();
    return baos.toByteArray();
}

}
