package dao;
import java.util.List;
import model.Admin;
import util.*;
import java.util.Optional;

public interface AdminDAO extends GenericUserDAO<Admin>, PasswordUpdatabale {

    void save(Admin admin) throws DAOExeption;

    Optional<Admin> findByUsername(String username) throws DAOExeption;

    Optional<Admin> findByUsernameOrEmail(String input) throws DAOExeption;

    int countAdmins() throws DAOExeption;

    void verify(String userId) throws DAOExeption;

    int getMaxAdminIdNumber() throws DAOExeption;
    
    List<String> findAllAdminEmails() throws DAOExeption;
    void update(Admin admin) throws DAOExeption;
    
    List<Admin> findAll() throws DAOExeption;

     
}
