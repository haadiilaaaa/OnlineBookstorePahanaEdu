package model;

public interface User {
    String getId();
    String getUsername();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getContactNumber();
    String getPasswordHash();
    boolean isVerified();
}
