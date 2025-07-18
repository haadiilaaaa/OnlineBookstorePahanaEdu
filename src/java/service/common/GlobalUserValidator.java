package service.common;
import java.util.Optional;
import dao.GenericUserDAO;
import model.User;
import util.ValidationException;

import java.util.List;

public class GlobalUserValidator {

    private final List<GenericUserDAO<? extends User>> userDAOs;

    public GlobalUserValidator(List<GenericUserDAO<? extends User>> userDAOs) {
        this.userDAOs = userDAOs;
    }

    // For registration (no current user to exclude)
public void validateUniqueUsernameAndEmail(String username, String email) throws Exception {
    validateUniqueUsernameAndEmail(username, email, null);
}

// For update (exclude current user)
public void validateUniqueUsernameAndEmail(String username, String email, String currentUserId) throws Exception {
    for (GenericUserDAO<? extends User> dao : userDAOs) {
        Optional<? extends User> userByUsername = dao.findByUsername(username);
        if (userByUsername.isPresent() && !userByUsername.get().getId().equals(currentUserId)) {
            throw new ValidationException("Username is already taken.");
        }

        Optional<? extends User> userByEmail = dao.findByEmail(email);
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(currentUserId)) {
            throw new ValidationException("Email is already registered.");
        }
    }
}


}
