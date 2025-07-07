package dao;

import model.Customer;

public interface CustomerDAO {
    void save(Customer customer) throws Exception;
    Customer findByEmail(String email) throws Exception;
    Customer findByUsername(String username) throws Exception;
    int countCustomers() throws Exception;
     void verify(String userId) throws Exception;
      int getMaxCustomerIdNumber() throws Exception;  // ✅ Add this method
      Customer findById(String id) throws Exception;
      Customer findByUsernameOrEmail(String input) throws Exception;
      void updatePassword(String userId, String hashedPassword) throws Exception;



}
