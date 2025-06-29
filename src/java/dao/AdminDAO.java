package dao;

import model.Admin;

public interface AdminDAO {
    void save(Admin admin) throws Exception;
    Admin findByEmail(String email) throws Exception;
    Admin findByUsername(String username) throws Exception;
    int countAdmins() throws Exception;
     void verify(String userId) throws Exception;
}
