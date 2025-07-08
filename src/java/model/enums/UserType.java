package model.enums;

public enum UserType {
    CUSTOMER("customer"),
    ADMIN("admin"),
    STAFF("staff");

    private final String type;

    UserType(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }

    public static boolean isValid(String type) {
        for (UserType u : values()) {
            if (u.getValue().equalsIgnoreCase(type)) return true;
        }
        return false;
    }
}
