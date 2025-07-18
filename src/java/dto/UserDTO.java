package dto;

public class UserDTO {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String contactNumber;
    private String role;
    private boolean verified; // If OTP verified

    public UserDTO() {}

    public UserDTO(String id, String username, String fullName, String email, String contactNumber, String role, boolean verified) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.role = role;
        this.verified = verified;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
}
