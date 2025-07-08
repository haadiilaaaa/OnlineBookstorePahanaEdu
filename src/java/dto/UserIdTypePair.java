
package dto;


public class UserIdTypePair {
    private final String userId;
    private final String userType;

    public UserIdTypePair(String userId, String userType) {
        this.userId = userId;
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }
}
