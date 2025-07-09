package listener;

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
import strategy.admin.item.*;


import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("✅ AppContextListener started and StrategyContext initialized.");

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            // Initialize DAOs for user services
            CustomerDAO customerDAO = new CustomerDAOimpl(connection);
            AdminDAO adminDAO = new AminDAOImpl(connection);
            StaffDAO staffDAO = new StaffDAOImpl(connection);
            OtpTokenDAO otpDAO = new OtpTokenDAOImpl(connection);

            // Common services
            InputValidationService inputValidationService = new InputValidationServiceImpl();

            OtpSender otpEmailService = EmailServiceFactory.createOtpEmailService();
            EmailSender generalEmailService = EmailServiceFactory.createGeneralEmailService();

            OtpSendServiceImpl otpService = new OtpSendServiceImpl(otpDAO, otpEmailService);
            GlobalUserValidator globalValidator = new GlobalUserValidator(customerDAO, adminDAO, staffDAO);

            // Customer registration service and strategy
            RegisterCustomerService customerService =
                new RegisterCustomerServiceImpl(customerDAO, inputValidationService, otpService, globalValidator);
            RegistrationStrategy customerStrategy = new CustomerStrategy(customerService);

            // Admin registration service and strategy
            RegisterServiceAdmin adminService =
                new RegisterAdminServiceImpl(adminDAO, inputValidationService, otpService, globalValidator);
            RegistrationStrategy adminStrategy = new AdminStrategy(adminService);

            // Staff registration service and strategy
            RegisterStaffService staffService =
                new RegisterStaffServiceImpl(staffDAO, inputValidationService, otpService, globalValidator);
            RegistrationStrategy staffStrategy = new StaffStrategy(staffService);

            // Register all registration strategies
            StrategyContext strategyContext = new StrategyContext();
            strategyContext.addStrategy("customer", customerStrategy);
            strategyContext.addStrategy("admin", adminStrategy);
            strategyContext.addStrategy("staff", staffStrategy);

            // Save shared services to servlet context
            sce.getServletContext().setAttribute("StrategyContext", strategyContext);
            sce.getServletContext().setAttribute("GeneralEmailService", generalEmailService);

            // === OTP Verification Service and Redirect Strategies setup ===
            
             Map<String, UserVerificationStrategy> userVerificationStrategies = new HashMap<>();
userVerificationStrategies.put("customer", new CustomerVerificationStrategy(customerDAO));
userVerificationStrategies.put("admin", new AdminVerificationStrategy(adminDAO));
userVerificationStrategies.put("staff", new StaffVerificationStrategy(staffDAO));

UserVerificationStrategyContext verificationStrategyContext = new UserVerificationStrategyContext(userVerificationStrategies);

            // === OTP Verification Service ===
          OtpVerificationService otpVerificationService = new OtpVerificationServiceImpl(
        otpDAO, verificationStrategyContext);

// Register it in the servlet context so the servlet can get it
sce.getServletContext().setAttribute("OtpVerificationService", otpVerificationService);


            // === OTP Redirect Strategies ===
            Map<String, OtpRedirectStrategy> otpRedirectStrategies = new HashMap<>();
            otpRedirectStrategies.put("customer", new CustomerOtpRedirectStrategy());
            otpRedirectStrategies.put("admin", new AdminOtpRedirectStrategy());
            otpRedirectStrategies.put("staff", new StaffOtpRedirectStrategy());
            

            // 5. Save OTP redirect strategies map in servlet context
            sce.getServletContext().setAttribute("OtpRedirectStrategies", otpRedirectStrategies);

            // === New: create and register admin/item services ===
             // === Add this block to initialize and register AdminDashboardService ===
        try {
            System.out.println("Registering AdminDashoardService...");
            AdminDashoardService adminDashboardService = AdminDashboardServiceFactory.createDashboardService(connection);
            System.out.println("AdminDashoardService created: " + adminDashboardService);
            sce.getServletContext().setAttribute("AdminDashoardService", adminDashboardService);
            System.out.println("✅ AdminDashboardService registered in ServletContext.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
    AdminOrderService adminOrderService = AdminOrderServicefactory.createAdminOrderService(connection);
    sce.getServletContext().setAttribute("AdminOrderService", adminOrderService);
    System.out.println("✅ AdminOrderService registered in ServletContext.");
} catch (Exception e) {
    System.err.println("❌ Failed to initialize AdminOrderService.");
    e.printStackTrace();
}


            // DAOs needed by ItemService
            ItemDAO itemDAO = new ItemDAOImpl(connection);
            CategoryDAO categoryDAO = new CategoryDAOImpl(connection);

            // Mappers needed by services
            ItemMapper itemMapper = new ItemMapper();
            CategoryMapper categoryMapper = new CategoryMapper();

            DiscountDAO discountDAO = new DicountDAOimpl(connection);
            DiscountAssignmentDAO discountAssignmentDAO = new DiscountAssignmentDAOImpl(connection);

            // Create services with dependencies
            ItemService itemService = new ItemServiceImpl(itemDAO, categoryDAO, itemMapper, discountDAO,
                    discountAssignmentDAO);
            CategoryService categoryService = new CategoryServiceImpl(categoryDAO, categoryMapper);

          // after you create itemService and categoryService
sce.getServletContext().setAttribute("ItemService", itemService);
sce.getServletContext().setAttribute("CategoryService", categoryService);
// After creating categoryService:
CategoryCommandFactory categoryCommandFactory = new CategoryCommandFactory(categoryService);
sce.getServletContext().setAttribute("CategoryCommandFactory", categoryCommandFactory);
System.out.println("✅ CategoryCommandFactory registered in ServletContext.");


// still register in your custom service manager if needed
AddItemServiceManager.register(ItemService.class, itemService);
AddItemServiceManager.register(CategoryService.class, categoryService);

            System.out.println("✅ ItemService and CategoryService registered in AddItemServiceManager.");
            // After registering ItemService and CategoryService
Map<String, ItemActionStrategy> itemStrategyMap = ItemStrategyRegistrar.registerAll(itemService, categoryService);
sce.getServletContext().setAttribute("ItemStrategyMap", itemStrategyMap);
System.out.println("✅ ItemStrategyMap registered in ServletContext.");

DiscountManagementService discountManagementService = new DiscountManagementServiceImpl(
    discountDAO,
    discountAssignmentDAO,
    itemDAO,
    categoryDAO
);
sce.getServletContext().setAttribute("DiscountManagementService", discountManagementService);
System.out.println("✅ DiscountManagementService registered in ServletContext.");


        } catch (Exception e) {
            e.printStackTrace();
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
