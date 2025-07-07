package controller.customer;

import dto.OrderDTO;
import dto.OrderItemDTO;
import dto.UserSession;
import model.CartItem;
import service.customer.*;
import util.IDGenerator;
import dao.OrderDAOImpl;
import dao.OrderItemDAOImpl;
import db.DBConnection;
import util.EmailSender;
import util.EmailServiceFactory;
import dao.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;
import model.OrderItem;
import util.PDFInvoiceGenerator;
import dao.CartItemDAOimpl;
import java.io.FileOutputStream;
import strategy.payment.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



        

public class PlaceOrderServlet extends HttpServlet {

    private OrderPlacementService orderPlacementService;
    private InvoiceService invoiceService;
    private OrderItemDAO orderItemDAO; // ✅ Declare here

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();

            OrderDAO orderDAO = new OrderDAOImpl(conn);
            this.orderItemDAO = new OrderItemDAOImpl(conn); // ✅ assign to field

            this.invoiceService = new InvoiceServiceImpl();

            EmailSender emailSender = EmailServiceFactory.createGeneralEmailService();
            OrderEmailService emailService = new OrderEmailServiceImpl(emailSender);

            this.orderPlacementService = new OrderPlacementServiceImpl(
                orderDAO,
                this.orderItemDAO,  // ✅ use the same instance
                invoiceService,
                emailService
            );

        } catch (Exception e) {
            throw new ServletException("Failed to initialize PlaceOrderServlet", e);
        }
    }

   @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    HttpSession session = req.getSession();
    UserSession user = (UserSession) session.getAttribute("user");
    Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");

    if (user == null || cart == null || cart.isEmpty()) {
        resp.sendRedirect("customerDashboard.jsp");
        return;
    }

    String shippingAddress = req.getParameter("shippingAddress");
    String paymentMethod = req.getParameter("paymentMethod");

    try {
    int nextNumber = ((OrderDAOImpl)((OrderPlacementServiceImpl) orderPlacementService).getOrderDAO()).getNextOrderNumber();
    String orderId = IDGenerator.generateId("ORD", nextNumber);

    OrderDTO order = new OrderDTO();
    order.setOrderId(orderId);
    order.setUserId(user.getId());
    order.setCustomerName(user.getFullName());
    order.setEmail(user.getEmail());
    order.setShippingAddress(shippingAddress);
    order.setOrderDate(new Date());
    order.setPaymentMethod(paymentMethod);
    System.out.println("? Order placed with ID: " + orderId);

System.out.println("? Payment method: " + paymentMethod);

    
    
        // Prepare payment DAO to pass to payment strategies
        PaymentDAO paymentDAO = new PaymentDAOImpl(DBConnection.getInstance().getConnection());

        // Select payment strategy based on paymentMethod
        PaymentStrategy paymentStrategy;
        switch (paymentMethod) {
            case "Credit Card":
                paymentStrategy = new CreditCardPayment(
                    req.getParameter("cardNumber"),
                    req.getParameter("expiryDate"),
                    req.getParameter("cvv"),
                    paymentDAO
                );
                break;
            case "Debit Card":
                paymentStrategy = new DebitCardPayment(
                    req.getParameter("cardNumber"),
                    req.getParameter("expiryDate"),
                    req.getParameter("cvv"),
                    paymentDAO
                );
                break;
            case "Cash on Delivery":
            default:
                paymentStrategy = new CashOnDeliveryPayment(paymentDAO);
                break;
        }

        // Process payment via chosen strategy
        paymentStrategy.processPayment(order);

    BigDecimal total = BigDecimal.ZERO;
    List<OrderItemDTO> orderItems = new ArrayList<>();

   int orderItemNumber = ((OrderItemDAOImpl) orderItemDAO).getNextOrderItemNumber(); // e.g., returns 2

int counter = 0;
for (CartItem item : cart.values()) {
    String orderItemId = IDGenerator.generateId("OID", orderItemNumber + counter); // OID002, OID003, etc.
    counter++;

    OrderItemDTO orderItem = new OrderItemDTO();
    orderItem.setOrderItemId(orderItemId); // ✅ important!
    orderItem.setOrderId(orderId);
    orderItem.setItemId(item.getItemId());
    orderItem.setItemTitle(item.getItemTitle());
    orderItem.setPrice(item.getPrice());
    orderItem.setQuantity(item.getQuantity());

    BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    total = total.add(subtotal);

    orderItems.add(orderItem);
}


    order.setTotalAmount(total);
    order.setItems(orderItems);

    // 1. Generate invoice HTML string (for browser display & email body)
    String invoiceHtml = invoiceService.generateInvoice(order, orderItems);

    // 2. Generate PDF bytes for email attachment
    byte[] pdfBytes = PDFInvoiceGenerator.generateInvoicePDF(order, orderItems);

    // 3. Place order & send confirmation email with PDF attachment
    orderPlacementService.placeOrder(order, orderItems, user.getEmail(), invoiceHtml, pdfBytes);

  // Clear cart from session
session.removeAttribute("cart");

// Also clear from database
CartItemDAO cartItemDAO = new CartItemDAOimpl(DBConnection.getInstance().getConnection());
cartItemDAO.deleteCartItemsByUserId(user.getId());

// Save PDF file temporarily to allow browser download
String invoiceFolderPath = getServletContext().getRealPath("/invoices");
File invoiceDir = new File(invoiceFolderPath);
if (!invoiceDir.exists()) {
    invoiceDir.mkdirs();  // ✅ create if not exists
}

// Save PDF file temporarily to allow browser download
String fileName = "Invoice_" + order.getOrderId() + ".pdf";
String absolutePath = invoiceDir.getAbsolutePath() + File.separator + fileName;

try (FileOutputStream fos = new FileOutputStream(absolutePath)) {
    fos.write(pdfBytes);
    System.out.println("✅ Invoice PDF saved at: " + absolutePath);
} catch (IOException ex) {
    System.err.println("❌ Failed to write PDF: " + ex.getMessage());
    throw ex; // Optional: rethrow or handle gracefully
}

req.setAttribute("order", order);
req.setAttribute("invoice", invoiceHtml);
req.setAttribute("invoiceDownloadPath", "invoices/" + fileName);

// DEBUG logs
System.out.println("✅ Redirecting to thankyou.jsp");
System.out.println("✅ User in session: " + session.getAttribute("user"));
System.out.println("✅ UserType in session: " + session.getAttribute("userType"));

req.getRequestDispatcher("thankyou.jsp").forward(req, resp);



} catch (Exception e) {
    e.printStackTrace();
    req.setAttribute("error", "Failed to place order. Please try again.");
    req.getRequestDispatcher("checkout.jsp").forward(req, resp);
}

}
}