package service.common;

import dao.*;
import model.*;
import dto.UserSession;
import util.PasswordHasher;

public class LoginServiceImpl implements LoginService {

    private final CustomerDAO customerDAO;
    private final AdminDAO adminDAO;
    private final StaffDAO staffDAO;

    public LoginServiceImpl() throws Exception {
        var conn = db.DBConnection.getInstance().getConnection();
        this.customerDAO = new CustomerDAOimpl(conn);
        this.adminDAO = new AminDAOImpl(conn);  // ✅ Make sure class is named correctly: AdminDAOImpl
        this.staffDAO = new StaffDAOImpl(conn);
    }

   @Override
public UserSession authenticate(String usernameOrEmail, String password) throws Exception {

    // Customer
    Customer customer = customerDAO.findByUsernameOrEmail(usernameOrEmail);
    if (customer != null && PasswordHasher.verifyPassword(password, customer.getPasswordHash())) {
        return new UserSession(
            customer.getId(),
            customer.getUsername(),
            customer.getEmail(),
            "customer",
            customer.getFirstName(),
            customer.getLastName(),
            customer.getContactNumber(),
            customer.getAddress()
        );
    }

    // Admin
    Admin admin = adminDAO.findByUsernameOrEmail(usernameOrEmail);
    if (admin != null && PasswordHasher.verifyPassword(password, admin.getPasswordHash())) {
        return new UserSession(admin.getId(), admin.getUsername(), admin.getEmail(), "admin", null, null, null, null);
    }

    // Staff
    Staff staff = staffDAO.findByUsernameOrEmail(usernameOrEmail);
    if (staff != null && PasswordHasher.verifyPassword(password, staff.getPasswordHash())) {
        return new UserSession(staff.getId(), staff.getUsername(), staff.getEmail(), "staff", null, null, null, null);
    }

    return null;
}

}
