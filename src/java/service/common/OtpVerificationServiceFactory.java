package service.common;

import dao.*;
import db.DBConnection;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class OtpVerificationServiceFactory {

    public static OtpVerificationService createService() throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();

        OtpTokenDAO otpTokenDAO = new OtpTokenDAOImpl(connection);
        CustomerDAO customerDAO = new CustomerDAOimpl(connection);
        AdminDAO adminDAO = new AminDAOImpl(connection);
        StaffDAO staffDAO = new StaffDAOImpl(connection);

        // Create strategies
        Map<String, UserVerificationStrategy> strategyMap = new HashMap<>();
        strategyMap.put("customer", new CustomerVerificationStrategy(customerDAO));
        strategyMap.put("admin", new AdminVerificationStrategy(adminDAO));
        strategyMap.put("staff", new StaffVerificationStrategy(staffDAO));

        UserVerificationStrategyContext strategyContext = new UserVerificationStrategyContext(strategyMap);

        return new OtpVerificationServiceImpl(otpTokenDAO, strategyContext);
    }
}
