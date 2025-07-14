package service.common;

import dao.*;
import db.DBConnection;
import service.common.*;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class LoginServiceFactory {

    public static LoginService createLoginService() {
        try {
            Connection conn = DBConnection.getInstance().getConnection();

            CustomerDAO customerDAO = new CustomerDAOimpl(conn);
            AdminDAO adminDAO = new AminDAOImpl(conn);
            StaffDAO staffDAO = new StaffDAOImpl(conn);

            List<Authenticator> authenticators = Arrays.asList(
                new CustomerAuthenticator(customerDAO),
                new AdminAuthenticator(adminDAO),
                new StaffAuthenticator(staffDAO)
            );

            return new LoginServiceImpl(authenticators);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create LoginServiceImpl", e);
        }
    }
}
