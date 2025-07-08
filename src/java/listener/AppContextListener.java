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

            // Register in your service manager for retrieval by servlets
            AddItemServiceManager.register(ItemService.class, itemService);
            AddItemServiceManager.register(CategoryService.class, categoryService);

            System.out.println("✅ ItemService and CategoryService registered in AddItemServiceManager.");

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
