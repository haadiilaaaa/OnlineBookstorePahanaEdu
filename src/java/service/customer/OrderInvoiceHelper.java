package service.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;
import dto.UserSession;
import service.customer.InvoiceService;
import dao.ItemDAO;
import model.Item;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

// OrderInvoiceHelper.java (only the part relevant to setting the attributes)

public class OrderInvoiceHelper {

    public static void prepareInvoiceData(
            HttpServletRequest req,
            OrderDTO order,
            UserSession user,
            List<OrderItemDTO> items,
            InvoiceService invoiceService,
            ItemDAO itemDAO
    ) throws Exception {

        // ✅ Set user details from session
        order.setCustomerName(user.getFullName());
        order.setEmail(user.getEmail());
        order.setShippingAddress(user.getAddress());

        // ✅ Populate item titles
        List<OrderItemDTO> validItems = new ArrayList<>();
        for (OrderItemDTO item : items) {
            if (item.getItemId() != null && !item.getItemId().isBlank()) {
                Item itemDetails = itemDAO.findById(item.getItemId());
                if (itemDetails != null) {
                    item.setItemTitle(itemDetails.getTitle());
                } else {
                    item.setItemTitle("Unknown Item");
                }
                validItems.add(item);
            } else {
                System.out.println("⚠️ Skipping item with null/empty itemId: " + item);
            }
        }

        order.setItems(validItems);

        // ✅ Calculate total
        BigDecimal total = validItems.stream()
                .filter(i -> i.getPrice() != null)
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        // ✅ Generate invoice HTML
        String invoiceHtml = invoiceService.generateInvoice(order, validItems);
        order.setInvoiceHtml(invoiceHtml);
        order.setInvoiceDownloadPath("invoices/Invoice_" + order.getOrderId() + ".pdf");

        // ✅ Set request attributes (used in thank_you.jsp)
        req.setAttribute("order", order);
        req.setAttribute("invoice", invoiceHtml);
        req.setAttribute("invoiceDownloadPath", order.getInvoiceDownloadPath());
    }
}
