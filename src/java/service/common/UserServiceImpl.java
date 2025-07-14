package service.common;

import dao.GenericUserDAO;
import model.User;
import dto.UserIdTypePair;

import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final Map<String, GenericUserDAO<? extends User>> userDAOs;

    public UserServiceImpl(Map<String, GenericUserDAO<? extends User>> userDAOs) {
        this.userDAOs = userDAOs;
    }

    @Override
    public String getEmailByUserIdAndType(String userId, String userType) throws Exception {
        GenericUserDAO<? extends User> dao = userDAOs.get(userType.toLowerCase());
        if (dao == null) {
            throw new IllegalArgumentException("Unsupported user type: " + userType);
        }

        return dao.findById(userId)
                  .map(User::getEmail)
                  .orElse(null);
    }

    @Override
    public Optional<UserIdTypePair> findUserIdAndTypeByEmail(String email) {
        for (Map.Entry<String, GenericUserDAO<? extends User>> entry : userDAOs.entrySet()) {
            try {
                Optional<? extends User> userOpt = entry.getValue().findByEmail(email);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    return Optional.of(new UserIdTypePair(user.getId(), entry.getKey()));
                }
            } catch (Exception e) {
                e.printStackTrace(); // 🔧 Replace with proper logging
            }
        }
        return Optional.empty();
    }
}
