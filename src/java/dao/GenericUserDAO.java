package dao;

import model.User;
import java.util.Optional;
import util.*;

public interface GenericUserDAO<T extends User> {
    Optional<T> findById(String id) throws DAOExeption;
    Optional<T> findByEmail(String email) throws DAOExeption;
    Optional<T> findByUsername(String username) throws DAOExeption;
}
