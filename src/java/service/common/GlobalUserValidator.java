package service.common;

import dao.GenericUserDAO;
import model.User;
import util.ValidationException;

import java.util.List;

public class GlobalUserValidator {

    private final List<GenericUserDAO<? extends User>> userDAOs;

    public GlobalUserValidator(List<GenericUserDAO<? extends User>> userDAOs) {
        this.userDAOs = userDAOs;
    }

    public void validateUniqueUsernameAndEmail(String username, String email) throws Exception {
        for (GenericUserDAO<? extends User> dao : userDAOs) {
            if (dao.findByUsername(username).isPresent()) {
                throw new ValidationException("Username is already taken.");
            }
            if (dao.findByEmail(email).isPresent()) {
                throw new ValidationException("Email is already registered.");
            }
        }
    }
}
