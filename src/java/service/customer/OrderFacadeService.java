package service.customer;

import dao.PaymentDAO;
import dto.OrderDTO;
import dto.UserSession;
import model.CartItem;
import strategy.payment.PaymentStrategy;
import strategy.payment.PaymentStrategyFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

public class OrderFacadeService {

    private final OrderPreparationService orderPreparationService;
    private final InvoiceStorageService invoiceStorageService;
    private final PaymentProcessingService paymentProcessingService;
    private final PaymentDAO paymentDAO;

    public OrderFacadeService(OrderPreparationService orderPreparationService,
                              InvoiceStorageService invoiceStorageService,
                              PaymentProcessingService paymentProcessingService,
                              PaymentDAO paymentDAO) {
        this.orderPreparationService = orderPreparationService;
        this.invoiceStorageService = invoiceStorageService;
        this.paymentProcessingService = paymentProcessingService;
        this.paymentDAO = paymentDAO;
    }

    public OrderDTO processOrder(HttpServletRequest req,
                                 UserSession user,
                                 Map<String, CartItem> cart,
                                 String shippingAddress,
                                 String paymentMethod) throws Exception {

        // Step 1: Calculate delivery fare (flat fee or dynamic)
        BigDecimal deliveryFare = new BigDecimal("250.00"); // for now, fixed flat rate

        // Step 2: Prepare Order including delivery fare
        OrderDTO order = orderPreparationService.prepareOrder(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                shippingAddress,
                paymentMethod,
                cart,
                deliveryFare
        );

        // Step 3: Generate invoice and store PDF
        byte[] pdfBytes = invoiceStorageService.generateAndStoreInvoice(req, order);

        // Step 4: Payment strategy creation
        PaymentStrategy strategy = PaymentStrategyFactory.getStrategy(paymentMethod, req, paymentDAO);

        // Step 5: Process payment, save order, clear cart
        paymentProcessingService.process(order, strategy, user.getEmail(), cart, pdfBytes);

        return order;
    }
}
