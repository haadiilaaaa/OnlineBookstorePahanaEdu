package dao;

import model.User;

public interface GenericUserDAO {
    User findById(String id) throws Exception;
     User findByEmail(String email) throws Exception;
}
