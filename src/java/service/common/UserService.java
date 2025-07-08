package service.common;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import dto.*;

public interface UserService {
    String getEmailByUserIdAndType(String userId, String userType) throws Exception;
    Optional<UserIdTypePair> findUserIdAndTypeByEmail(String email);

}
