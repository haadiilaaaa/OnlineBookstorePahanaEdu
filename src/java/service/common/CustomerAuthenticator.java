package service.common;

import dao.CustomerDAO;
import model.Customer;
import util.PasswordHasher;
import dto.UserSession;

import java.util.Optional;

public class CustomerAuthenticator implements Authenticator {

    private final CustomerDAO customerDAO;

    public CustomerAuthenticator(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public Optional<UserSession> authenticate(String usernameOrEmail, String password) {
        try {
            return customerDAO.findByUsernameOrEmail(usernameOrEmail)
                    .filter(customer -> PasswordHasher.verifyPassword(password, customer.getPasswordHash()))
                    .map(customer -> new UserSession(
                            customer.getId(),
                            customer.getUsername(),
                            customer.getEmail(),
                            "customer",
                            customer.getFirstName(),
                            customer.getLastName(),
                            customer.getContactNumber(),
                            customer.getAddress()
                    ));
        } catch (Exception e) {
            return Optional.empty(); // optionally log
        }
    }
}
