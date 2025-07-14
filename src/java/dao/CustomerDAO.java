package dao;

import model.Customer;
import java.util.Optional;
import util.*;

public interface CustomerDAO extends GenericUserDAO<Customer>, PasswordUpdatabale {

    void save(Customer customer) throws DAOExeption;

    Optional<Customer> findByUsername(String username) throws DAOExeption;

    int countCustomers() throws DAOExeption;

    void verify(String userId) throws DAOExeption;

    int getMaxCustomerIdNumber() throws DAOExeption;

    Optional<Customer> findByUsernameOrEmail(String input) throws DAOExeption;

    void updatePassword(String userId, String hashedPassword) throws DAOExeption;
}
