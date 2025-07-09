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

            // === DAO INITIALIZATION ===
            CustomerDAO customerDAO = new CustomerDAOimpl(connection);
            AdminDAO adminDAO = new AminDAOImpl(connection);
            StaffDAO staffDAO = new StaffDAOImpl(connection);
            OtpTokenDAO otpDAO = new OtpTokenDAOImpl(connection);
            CartItemDAO cartItemDAO = new CartItemDAOimpl(connection);
            DiscountDAO discountDAO = new DicountDAOimpl(connection);
            DiscountAssignmentDAO discountAssignmentDAO = new DiscountAssignmentDAOImpl(connection);
            CategoryDAO categoryDAO = new CategoryDAOImpl(connection);
            ItemDAO itemDAO = new ItemDAOImpl(connection);

            // === COMMON SERVICES ===
            InputValidationService inputValidationService = new InputValidationServiceImpl();
            OtpSender otpEmailService = EmailServiceFactory.createOtpEmailService();
            EmailSender generalEmailService = EmailServiceFactory.createGeneralEmailService();
            OtpSendServiceImpl otpService = new OtpSendServiceImpl(otpDAO, otpEmailService);
            GlobalUserValidator globalValidator = new GlobalUserValidator(customerDAO, adminDAO, staffDAO);

            // === REGISTRATION STRATEGIES ===
            RegisterCustomerService customerService = new RegisterCustomerServiceImpl(
                    customerDAO, inputValidationService, otpService, globalValidator);
            RegisterServiceAdmin adminService = new RegisterAdminServiceImpl(
                    adminDAO, inputValidationService, otpService, globalValidator);
            RegisterStaffService staffService = new RegisterStaffServiceImpl(
                    staffDAO, inputValidationService, otpService, globalValidator);

            StrategyContext strategyContext = new StrategyContext();
            strategyContext.addStrategy("customer", new CustomerStrategy(customerService));
            strategyContext.addStrategy("admin", new AdminStrategy(adminService));
            strategyContext.addStrategy("staff", new StaffStrategy(staffService));

            // === STORE STRATEGY CONTEXT ===
            sce.getServletContext().setAttribute("StrategyContext", strategyContext);
            sce.getServletContext().setAttribute("GeneralEmailService", generalEmailService);

            // === OTP VERIFICATION STRATEGIES ===
            Map<String, UserVerificationStrategy> userVerificationStrategies = new HashMap<>();
            userVerificationStrategies.put("customer", new CustomerVerificationStrategy(customerDAO));
            userVerificationStrategies.put("admin", new AdminVerificationStrategy(adminDAO));
            userVerificationStrategies.put("staff", new StaffVerificationStrategy(staffDAO));
            UserVerificationStrategyContext verificationStrategyContext = new UserVerificationStrategyContext(userVerificationStrategies);

            // === OTP VERIFICATION SERVICE ===
            OtpVerificationService otpVerificationService = new OtpVerificationServiceImpl(otpDAO, verificationStrategyContext);
            sce.getServletContext().setAttribute("OtpVerificationService", otpVerificationService);

            // === OTP REDIRECT STRATEGIES ===
            Map<String, OtpRedirectStrategy> otpRedirectStrategies = new HashMap<>();
            otpRedirectStrategies.put("customer", new CustomerOtpRedirectStrategy());
            otpRedirectStrategies.put("admin", new AdminOtpRedirectStrategy());
            otpRedirectStrategies.put("staff", new StaffOtpRedirectStrategy());
            sce.getServletContext().setAttribute("OtpRedirectStrategies", otpRedirectStrategies);

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

            ItemService itemService = new ItemServiceImpl(itemDAO, categoryDAO, itemMapper, discountDAO, discountAssignmentDAO);
            CategoryService categoryService = new CategoryServiceImpl(categoryDAO, categoryMapper);

            sce.getServletContext().setAttribute("ItemService", itemService);
            sce.getServletContext().setAttribute("CategoryService", categoryService);

            CategoryCommandFactory categoryCommandFactory = new CategoryCommandFactory(categoryService);
            sce.getServletContext().setAttribute("CategoryCommandFactory", categoryCommandFactory);
            System.out.println("✅ CategoryCommandFactory registered.");

            AddItemServiceManager.register(ItemService.class, itemService);
            AddItemServiceManager.register(CategoryService.class, categoryService);
            System.out.println("✅ ItemService & CategoryService registered in AddItemServiceManager.");

            Map<String, ItemActionStrategy> itemStrategyMap = ItemStrategyRegistrar.registerAll(itemService, categoryService);
            sce.getServletContext().setAttribute("ItemStrategyMap", itemStrategyMap);
            System.out.println("✅ ItemStrategyMap registered.");

            // === DISCOUNT MANAGEMENT SERVICE ===
            DiscountManagementService discountManagementService = new DiscountManagementServiceImpl(
                    discountDAO, discountAssignmentDAO, itemDAO, categoryDAO);
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
