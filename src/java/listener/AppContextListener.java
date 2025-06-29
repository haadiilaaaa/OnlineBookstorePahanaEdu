package listener;

import dao.*;
import db.DBConnection;
import service.common.*;
import strategy.*;
import util.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            // Initialize DAOs
            CustomerDAO customerDAO = new CustomerDAOimpl(connection);
            AdminDAO adminDAO = new AminDAOImpl(connection);
            StaffDAO staffDAO = new StaffDAOImpl(connection);
            OtpTokenDAO otpDAO = new OtpTokenDAOImpl(connection);

            // Common services
            InputValidationService inputValidationService = new InputValidationServiceImpl();
            OtpSendService otpSendService = new OtpSendServiceImpl(otpDAO);

            // Registration strategies
            RegistrationStrategy customerStrategy = new CustomerStrategy(customerDAO, inputValidationService, otpSendService);
            RegistrationStrategy adminStrategy = new AdminStrategy(adminDAO, inputValidationService, otpSendService);
            RegistrationStrategy staffStrategy = new StaffStrategy(staffDAO, inputValidationService, otpSendService);

            // Strategy context
            StrategyContext strategyContext = new StrategyContext();
            strategyContext.addStrategy("customer", customerStrategy);
            strategyContext.addStrategy("admin", adminStrategy);
            strategyContext.addStrategy("staff", staffStrategy);

            // Save to context
            sce.getServletContext().setAttribute("StrategyContext", strategyContext);

        } catch (Exception e) {
            e.printStackTrace(); // or log the error
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // cleanup if needed
    }
}
