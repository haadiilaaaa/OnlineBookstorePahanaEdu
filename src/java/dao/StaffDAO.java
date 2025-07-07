package dao;

import model.Admin;
import model.Staff;

public interface StaffDAO {
    void save(Staff staff) throws Exception;
    Staff findByEmail(String email) throws Exception;
    Staff findByUsername(String username) throws Exception;
    int countStaff() throws Exception;
     void verify(String userId) throws Exception;
     int getMaxStaffIdNumber() throws Exception;  // ✅ Add this method
     Staff findById(String id) throws Exception;
      Staff findByUsernameOrEmail(String input) throws Exception;
      void updatePassword(String userId, String hashedPassword) throws Exception;

}
//staff data access layer