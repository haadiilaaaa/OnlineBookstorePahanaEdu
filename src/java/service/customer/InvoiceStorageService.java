package service.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;
import util.PDFInvoiceGenerator;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class InvoiceStorageService {

    private final InvoiceService invoiceService;

    public InvoiceStorageService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public byte[] generateAndStoreInvoice(HttpServletRequest req, OrderDTO order) throws Exception {
        List<OrderItemDTO> items = order.getItems();

        String invoiceHtml = invoiceService.generateInvoice(order, items);
        byte[] pdfBytes = PDFInvoiceGenerator.generateInvoicePDF(order, items);

        // Save PDF to file system
        String invoicesDirPath = req.getServletContext().getRealPath("/invoices");
        File invoicesDir = new File(invoicesDirPath);
        if (!invoicesDir.exists()) {
            invoicesDir.mkdirs();
        }

        String fileName = "Invoice_" + order.getOrderId() + ".pdf";
        File invoiceFile = new File(invoicesDir, fileName);
        try (FileOutputStream fos = new FileOutputStream(invoiceFile)) {
            fos.write(pdfBytes);
        }

        order.setInvoiceHtml(invoiceHtml);
        order.setInvoiceDownloadPath("invoices/" + fileName);

        return pdfBytes;
    }
}
