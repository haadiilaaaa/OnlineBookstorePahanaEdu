package dao;

import model.Staff;
import java.util.Optional;
import util.DAOExeption;
import java.util.*;

public interface StaffDAO extends GenericUserDAO<Staff>, PasswordUpdatabale {

    void save(Staff staff) throws DAOExeption;

    Optional<Staff> findByEmail(String email) throws DAOExeption;

    Optional<Staff> findByUsername(String username) throws DAOExeption;

    int countStaff() throws DAOExeption;

    void verify(String userId) throws DAOExeption;

    int getMaxStaffIdNumber() throws DAOExeption;

    Optional<Staff> findById(String id) throws DAOExeption;

    Optional<Staff> findByUsernameOrEmail(String input) throws DAOExeption;

    void updatePassword(String userId, String hashedPassword) throws DAOExeption;
    List<Staff> findAll() throws DAOExeption;
}
