// Updated UserSession.java
package dto;

import java.io.Serializable;

public class UserSession implements Serializable {
    private String id;
    private String username;
    private String email;
    private String userType;

    // ✅ Add these fields
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String address;

    public UserSession(String id, String username, String email, String userType,
                       String firstName, String lastName, String contactNumber, String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    // Getters
    public String getId() { return id; }
    
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getUserType() { return userType; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getContactNumber() { return contactNumber; }
    public String getAddress() { return address; }
    public String getFullName() {
    return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
}

  public void setId (String id){
      
      this.id = id;
  }

}
