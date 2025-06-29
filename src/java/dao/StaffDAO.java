package dao;

import model.Staff;

public interface StaffDAO {
    void save(Staff staff) throws Exception;
    Staff findByEmail(String email) throws Exception;
    Staff findByUsername(String username) throws Exception;
    int countStaff() throws Exception;
     void verify(String userId) throws Exception;
}
