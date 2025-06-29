package model;

public class Staff implements User {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String passwordHash;
    private boolean isVerified;

    public Staff() {}

    @Override public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Override public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    @Override public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    @Override public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    @Override public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    @Override public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}
