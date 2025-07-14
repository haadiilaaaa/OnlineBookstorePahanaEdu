package service.common;

import dao.*;
import db.DBConnection;
import model.User;
import util.EmailSender;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordServiceFactory {

    public ForgotPasswordService create(EmailSender emailSender) throws Exception {
        // Open connection just to create DAOs
        Connection connection = DBConnection.getInstance().getConnection();

        Map<String, GenericUserDAO<? extends User>> daoMap = new HashMap<>();
        daoMap.put("customer", new CustomerDAOimpl(connection));
        daoMap.put("admin", new AminDAOImpl(connection));
        daoMap.put("staff", new StaffDAOImpl(connection));

        PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAOImpl(connection);

        return new ForgotPasswordService(daoMap, tokenDAO, emailSender);
    }
}
