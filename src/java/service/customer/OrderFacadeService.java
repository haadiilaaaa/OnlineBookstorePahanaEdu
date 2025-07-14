package service.customer;

import dao.PaymentDAO;
import dto.OrderDTO;
import dto.UserSession;
import model.CartItem;
import strategy.payment.PaymentStrategy;
import strategy.payment.PaymentStrategyFactory;

import javax.servlet.http.HttpServletRequest;
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

        // Step 1: Prepare Order
        OrderDTO order = orderPreparationService.prepareOrder(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                shippingAddress,
                paymentMethod,
                cart
        );

        // Step 2: Generate invoice and store PDF
        byte[] pdfBytes = invoiceStorageService.generateAndStoreInvoice(req, order);

        // Step 3: Payment strategy creation
        PaymentStrategy strategy = PaymentStrategyFactory.getStrategy(paymentMethod, req, paymentDAO);

        // Step 4: Process payment, save order, clear cart
        paymentProcessingService.process(order, strategy, user.getEmail(), cart, pdfBytes);

        return order;
    }
}
