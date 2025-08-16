package listener;

import handler.customer.BookBrowseHandler;
import dao.*;
import db.DBConnection;
import mapper.ItemMapper;
import mapper.CategoryMapper;
import service.admin.*;
import service.common.*;
import service.customer.*;
import service.staff.*;
import strategy.*;
import util.*;
import util.*;
import util.redirect.*;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import command.customer.cart.AddToCartCommand;
import strategy.admin.item.*;
import java.util.List;
import model.User;
import service.deliveryPartner.*;
import service.deliveryPartner.DeliveryPartnerRegistrationService;
import service.deliveryPartner.RegisterDeliveryPartnerServiceImpl;
import dao.DeliveryPartnerDAO;
import dao.DeliveryPartnerDAOImpl;
import model.DeliveryPartner;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import util.NextSequentialIDGenerator; // Import the concrete ID generator
import java.util.stream.Collectors;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("✅ AppContextListener started and StrategyContext initialized.");

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            sce.getServletContext().setAttribute("DBConnection", connection);

            // === DAO INITIALIZATION ===
            CustomerDAO customerDAO = new CustomerDAOimpl(connection);
            AdminDAO adminDAO = new AminDAOImpl(connection);
            StaffDAO staffDAO = new StaffDAOImpl(connection);
            OtpTokenDAO otpDAO = new OtpTokenDAOImpl(connection);
            CartItemDAO cartItemDAO = new CartItemDAOimpl(connection);
            DiscountDAO tempDiscountDAO = new DicountDAOimpl(connection, new NextSequentialIDGenerator("dis", List.of()));
            List<String> existingDiscountIds = tempDiscountDAO.findAll().stream()
                .map(d -> d.getId())
                .collect(java.util.stream.Collectors.toList());

            IDGenerator<String> discountIdGenerator = new NextSequentialIDGenerator("dis", existingDiscountIds);
            DiscountDAO discountDAO = new DicountDAOimpl(connection, discountIdGenerator);
            DiscountAssignmentDAO discountAssignmentDAO = new DiscountAssignmentDAOImpl(connection);
            CategoryDAO categoryDAO = new CategoryDAOImpl(connection);
            ItemDAO itemDAO = new ItemDAOImpl(connection);
            DeliveryPartnerDAO deliveryPartnerDAO = new DeliveryPartnerDAOImpl(connection);
            OrderDAO orderDAO = new OrderDAOImpl(connection, new OrderItemDAOImpl(connection));
            OrderItemDAO orderItemDAO = new OrderItemDAOImpl(connection);
            PaymentDAO paymentDAO = new PaymentDAOImpl(connection);

            // === ID GENERATORS ===
            IDGenerator<String> customerIdGenerator = new NextSequentialIDGenerator("CUS", customerDAO.findAll().stream().map(User::getId).collect(Collectors.toList()));
IDGenerator<String> adminIdGenerator = new NextSequentialIDGenerator("ADM", adminDAO.findAll().stream().map(User::getId).collect(Collectors.toList()));
IDGenerator<String> staffIdGenerator = new NextSequentialIDGenerator("STF", staffDAO.findAll().stream().map(User::getId).collect(Collectors.toList()));
IDGenerator<String> deliveryPartnerIdGenerator = new NextSequentialIDGenerator("DP", deliveryPartnerDAO.findAll().stream().map(User::getId).collect(Collectors.toList()));

IDGenerator<String> orderIdGenerator = new ThreadSafeOrderIDOGenerator("ORD", orderDAO);
IDGenerator<String> cartIdGenerator = new ThreadSafeCartIDGenerator("CART", cartItemDAO);
System.out.println("DEBUG: ThreadSafeSequentialIDGenerator for cart items initialized.");

IDGenerator<String> paymentIdGenerator = new UUIDGenerator(); // Add this line if you have a PaymentDAO


IDGenerator<String> discountManagementIdGenerator = new NextSequentialIDGenerator("DIS", existingDiscountIds);

            // === COMMON SERVICES ===
            InputValidationService inputValidationService = new InputValidationServiceImpl();
            OtpSender otpEmailService = EmailServiceFactory.createOtpEmailService();
            EmailSender generalEmailService = EmailServiceFactory.createGeneralEmailService();
            OtpSendServiceImpl otpService = new OtpSendServiceImpl(otpDAO, otpEmailService);
            List<GenericUserDAO<? extends User>> userDAOs = List.of(customerDAO, adminDAO, staffDAO, deliveryPartnerDAO);
            GlobalUserValidator globalValidator = new GlobalUserValidator(userDAOs);

            // === REGISTRATION SERVICES ===
            RegisterCustomerService customerService = new RegisterCustomerServiceImpl(
                customerDAO, inputValidationService, otpService, globalValidator, customerIdGenerator);
            RegisterServiceAdmin adminService = new RegisterAdminServiceImpl(
                adminDAO, inputValidationService, otpService, globalValidator, adminIdGenerator);
            RegisterStaffService staffService = new RegisterStaffServiceImpl(
                staffDAO, inputValidationService, otpService, globalValidator, staffIdGenerator);
            DeliveryPartnerRegistrationService deliveryPartnerService = new RegisterDeliveryPartnerServiceImpl(
                deliveryPartnerDAO, inputValidationService, otpService, globalValidator, deliveryPartnerIdGenerator);

            // === REGISTRATION FACADE ===
            RegistrationFacadeService registrationFacadeService = new RegistrationFacadeServiceImpl(
                customerService, adminService, staffService, deliveryPartnerService);
            sce.getServletContext().setAttribute("RegistrationFacadeService", registrationFacadeService);
            System.out.println("✅ RegistrationFacadeService registered.");

            // === STRATEGY CONTEXT ===
            StrategyContext strategyContext = new StrategyContext();
            strategyContext.addStrategy("customer", new CustomerStrategy(customerService));
            strategyContext.addStrategy("admin", new AdminStrategy(adminService));
            strategyContext.addStrategy("staff", new StaffStrategy(staffService));
            strategyContext.addStrategy("delivery", new DeliveryStrategy(deliveryPartnerService));
            sce.getServletContext().setAttribute("StrategyContext", strategyContext);
            sce.getServletContext().setAttribute("GeneralEmailService", generalEmailService);
            sce.getServletContext().setAttribute("EmailSender", generalEmailService);

            // === OTP VERIFICATION STRATEGIES ===
            Map<String, UserVerificationStrategy> userVerificationStrategies = new HashMap<>();
            userVerificationStrategies.put("customer", new CustomerVerificationStrategy(customerDAO));
            userVerificationStrategies.put("admin", new AdminVerificationStrategy(adminDAO));
            userVerificationStrategies.put("staff", new StaffVerificationStrategy(staffDAO));
            userVerificationStrategies.put("delivery", new DeliveryPartnerVerificationStrategy(deliveryPartnerDAO));
            UserVerificationStrategyContext verificationStrategyContext = new UserVerificationStrategyContext(userVerificationStrategies);

            // === OTP VERIFICATION SERVICE ===
            OtpVerificationService otpVerificationService = new OtpVerificationServiceImpl(otpDAO, verificationStrategyContext);
            sce.getServletContext().setAttribute("OtpVerificationService", otpVerificationService);

            // === OTP REDIRECT STRATEGIES ===
            Map<String, OtpRedirectStrategy> otpRedirectStrategies = new HashMap<>();
            otpRedirectStrategies.put("customer", new CustomerOtpRedirectStrategy());
            otpRedirectStrategies.put("admin", new AdminOtpRedirectStrategy());
            otpRedirectStrategies.put("staff", new StaffOtpRedirectStrategy());
            otpRedirectStrategies.put("delivery", new DeliveryPartnerOTPRedirectStrategy());
            sce.getServletContext().setAttribute("OtpRedirectStrategies", otpRedirectStrategies);

            Map<String, GenericUserDAO<? extends User>> userDAOMap = new HashMap<>();
            userDAOMap.put("customer", customerDAO);
            userDAOMap.put("admin", adminDAO);
            userDAOMap.put("staff", staffDAO);
            userDAOMap.put("delivery", deliveryPartnerDAO);

            PasswordResetTokenDAO passwordResetTokenDAO = new PasswordResetTokenDAOImpl(connection);
            ForgotPasswordService forgotPasswordService =
                new ForgotPasswordService(userDAOMap, passwordResetTokenDAO, generalEmailService);
            sce.getServletContext().setAttribute("ForgotPasswordService", forgotPasswordService);
            System.out.println("✅ ForgotPasswordService registered.");

            sce.getServletContext().setAttribute("PasswordResetTokenDAO", passwordResetTokenDAO);

            Map<String, PasswordUpdatabale> passwordUpdatables = new HashMap<>();
            passwordUpdatables.put("customer", customerDAO);
            passwordUpdatables.put("admin", adminDAO);
            passwordUpdatables.put("staff", staffDAO);
            passwordUpdatables.put("delivery", deliveryPartnerDAO);

            ResetPasswordService resetPasswordService = new ResetPasswordServiceImpl(passwordResetTokenDAO, passwordUpdatables);
            sce.getServletContext().setAttribute("ResetPasswordService", resetPasswordService);
            sce.getServletContext().setAttribute("PasswordResetTokenDAO", passwordResetTokenDAO);
            sce.getServletContext().setAttribute("UserPasswordServices", passwordUpdatables);

            // === ORDER PLACEMENT SERVICES ===
            OrderPreparationService orderPreparationService = new OrderPreparationService(orderDAO, orderItemDAO, orderIdGenerator);
            InvoiceService invoiceService = new InvoiceServiceImpl();
            InvoiceStorageService invoiceStorageService = new InvoiceStorageService(invoiceService);
            OrderEmailService orderEmailService = new OrderEmailServiceImpl(generalEmailService);
            PaymentProcessingService paymentProcessingService = new PaymentProcessingService(orderDAO, orderItemDAO, cartItemDAO, orderEmailService);

            sce.getServletContext().setAttribute("InvoiceService", invoiceService);
            sce.getServletContext().setAttribute("OrderDAO", orderDAO);
            sce.getServletContext().setAttribute("OrderItemDAO", orderItemDAO);
            sce.getServletContext().setAttribute("ItemDAO", itemDAO);
            sce.getServletContext().setAttribute("DeliveryPartnerDAO", deliveryPartnerDAO);
            DeliveryOrderService deliveryOrderService = new DeliveryOrderServiceImpl(orderDAO);
            sce.getServletContext().setAttribute("DeliveryOrderService", deliveryOrderService);
            System.out.println("✅ DeliveryOrderService registered.");

            OrderFacadeService orderFacadeService = new OrderFacadeService(
                orderPreparationService,
                invoiceStorageService,
                paymentProcessingService,
                paymentDAO
            );
            sce.getServletContext().setAttribute("OrderFacadeService", orderFacadeService);
            System.out.println("✅ OrderFacadeService registered.");

            // === ADMIN DASHBOARD SERVICE ===
            try {
                AdminDashoardService adminDashboardService = AdminDashboardServiceFactory.createDashboardService(connection);
                sce.getServletContext().setAttribute("AdminDashoardService", adminDashboardService);
                System.out.println("✅ AdminDashboardService registered.");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // === ADMIN ORDER SERVICE ===
            try {
                AdminOrderService adminOrderService = AdminOrderServicefactory.createAdminOrderService(connection);
                sce.getServletContext().setAttribute("AdminOrderService", adminOrderService);
                System.out.println("✅ AdminOrderService registered.");
            } catch (Exception e) {
                System.err.println("❌ Failed to initialize AdminOrderService.");
                e.printStackTrace();
            }

            // === ITEM & CATEGORY SERVICES ===
            ItemMapper itemMapper = new ItemMapper();
            CategoryMapper categoryMapper = new CategoryMapper();

            DiscountService discountService = new DiscountServiceImpl(discountAssignmentDAO, discountDAO);
            ItemService itemService = new ItemServiceImpl(itemDAO, categoryDAO, itemMapper, discountService);
            sce.getServletContext().setAttribute("ItemService", itemService);

            CategoryService categoryService = new CategoryServiceImpl(categoryDAO, categoryMapper);
            sce.getServletContext().setAttribute("CategoryService", categoryService);

            SessionCartManager sessionCartManager = new SessionCartManager();
           PersistentCartService persistentCartService = new CartServiceImpl(cartItemDAO, sessionCartManager, cartIdGenerator);
            SessionCartService sessionCartService = (SessionCartService) persistentCartService;

            CategoryCache categoryCache = new CategoryCache(categoryService, sce.getServletContext());

            BookBrowseHandler bookBrowseHandler = new BookBrowseHandler(itemService, categoryCache, sessionCartService);
            sce.getServletContext().setAttribute("BookBrowseHandler", bookBrowseHandler);
            System.out.println("✅ BookBrowseHandler registered.");

            CategoryCommandFactory categoryCommandFactory = new CategoryCommandFactory(categoryService);
            sce.getServletContext().setAttribute("CategoryCommandFactory", categoryCommandFactory);
            System.out.println("✅ CategoryCommandFactory registered.");

            AddItemServiceManager.register(ItemService.class, itemService);
            AddItemServiceManager.register(CategoryService.class, categoryService);
            System.out.println("✅ ItemService & CategoryService registered in AddItemServiceManager.");

            CartFacade cartFacade = new CartFacade(persistentCartService, sessionCartService, discountService, itemMapper);

            sce.getServletContext().setAttribute("CartFacade", cartFacade);
            System.out.println("✅ CartFacade registered.");
            sce.getServletContext().setAttribute("ItemDAO", itemDAO);
            sce.getServletContext().setAttribute("DiscountService", discountService);
            sce.getServletContext().setAttribute("AddToCartRequestValidator", new AddToCartRequestValidator());

            if (cartFacade == null) {
                System.err.println("❌ Critical: CartFacade not found. AddToCartCommand cannot be initialized.");
            }

            AddToCartRequestValidator addToCartRequestValidator = new AddToCartRequestValidator();
            sce.getServletContext().setAttribute("AddToCartRequestValidator", addToCartRequestValidator);
            System.out.println("✅ AddToCartRequestValidator registered.");

            AddToCartCommand addToCartCommand = new AddToCartCommand(addToCartRequestValidator, itemService, cartFacade);
            sce.getServletContext().setAttribute("AddToCartCommand", addToCartCommand);
            System.out.println("✅ AddToCartCommand registered.");

            Map<String, ItemActionStrategy> itemStrategyMap = ItemStrategyRegistrar.registerAll(itemService, categoryService);
            sce.getServletContext().setAttribute("ItemStrategyMap", itemStrategyMap);
            System.out.println("✅ ItemStrategyMap registered.");

            // === DISCOUNT MANAGEMENT SERVICE ===
            DiscountManagementService discountManagementService = new DiscountManagementServiceImpl(
                discountDAO, discountAssignmentDAO, itemDAO, categoryDAO, discountIdGenerator, discountManagementIdGenerator);
            sce.getServletContext().setAttribute("DiscountManagementService", discountManagementService);
            System.out.println("✅ DiscountManagementService registered.");

            // === CUSTOMER DASHBOARD SERVICE ===
            CustomerDiscountService customerDiscountService = new CustomerDiscountService(discountAssignmentDAO, discountDAO);
            CustomerDashboardService customerDashboardService = new CustomerDashboardServiceImpl(
                customerDAO, cartItemDAO, discountDAO, categoryDAO, customerDiscountService, itemDAO);
            sce.getServletContext().setAttribute("CustomerDashboardService", customerDashboardService);
            System.out.println("✅ CustomerDashboardService registered.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ An error occurred during context initialization: " + e.getMessage());
            // It might be a good idea to re-throw as a ServletException to prevent the app from deploying in a broken state.
            throw new RuntimeException("Failed to initialize the application context.", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("⚠️ AppContextListener contextDestroyed called: cleaning up resources.");
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                System.out.println("Deregistered JDBC driver: " + driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        AbandonedConnectionCleanupThread.checkedShutdown();
        System.out.println("MySQL AbandonedConnectionCleanupThread shutdown.");
    }
}