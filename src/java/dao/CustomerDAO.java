package dao;

import model.Customer;

public interface CustomerDAO {
    void save(Customer customer) throws Exception;
    Customer findByEmail(String email) throws Exception;
    Customer findByUsername(String username) throws Exception;
    int countCustomers() throws Exception;
     void verify(String userId) throws Exception;
}
//Data access layer for customer