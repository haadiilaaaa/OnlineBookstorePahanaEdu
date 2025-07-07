package dao;

import model.Admin;
import model.Customer;

public interface AdminDAO {
    void save(Admin admin) throws Exception;
    Admin findByEmail(String email) throws Exception;
    Admin findByUsername(String username) throws Exception;
    int countAdmins() throws Exception;
    void verify(String userId) throws Exception;

    /** 
     * Gets the max numeric part of admin IDs (e.g., from adm__01, returns 1).
     */
    int getMaxAdminIdNumber() throws Exception;  // ✅ Add this method
    Admin findById(String id) throws Exception;
    Admin findByUsernameOrEmail(String input) throws Exception;
     void updatePassword(String userId, String hashedPassword) throws Exception;

}
