package service.common;

import dao.GenericUserDAO;
import model.User;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import java.util.Map;
import dto.*;
import dao.*;

public class UserServiceImpl implements UserService {

    private final Map<String, GenericUserDAO> userDAOs;

    public UserServiceImpl(Map<String, GenericUserDAO> userDAOs) {
        this.userDAOs = userDAOs;
    }

    @Override
    public String getEmailByUserIdAndType(String userId, String userType) throws Exception {
        GenericUserDAO dao = userDAOs.get(userType.toLowerCase());
        if (dao == null) {
            throw new IllegalArgumentException("Unsupported user type: " + userType);
        }

        User user = dao.findById(userId);
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }
      @Override
public Optional<UserIdTypePair> findUserIdAndTypeByEmail(String email) {
    for (Map.Entry<String, GenericUserDAO> entry : userDAOs.entrySet()) {
        try {
            var user = entry.getValue().findByEmail(email);
            if (user != null) {
                return Optional.of(new UserIdTypePair(user.getId(), entry.getKey()));
            }
        } catch (Exception e) {
            e.printStackTrace();  // or handle error properly
        }
    }
    return Optional.empty();
}



}
