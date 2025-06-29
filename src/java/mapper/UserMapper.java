package mapper;

import dto.CustomerDTO;
import dto.AdminDTO;
import dto.StaffDTO;
import model.Customer;
import model.Admin;
import model.Staff;

public class UserMapper {

    public static Customer toCustomer(CustomerDTO dto, String id, String hashedPassword) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setUsername(dto.getUsername());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setContactNumber(dto.getContactNumber());
        customer.setAddress(dto.getAddress());
        customer.setPasswordHash(hashedPassword);
        customer.setVerified(false);
        return customer;
    }

    public static Staff toStaff(StaffDTO dto, String id, String hashedPassword) {
        Staff staff = new Staff();
        staff.setId(id);
        staff.setUsername(dto.getUsername());
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setEmail(dto.getEmail());
        staff.setContactNumber(dto.getContactNumber());
        staff.setPasswordHash(hashedPassword);
        staff.setVerified(false);
        return staff;
    }

    public static Admin toAdmin(AdminDTO dto, String id, String hashedPassword) {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setUsername(dto.getUsername());
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setEmail(dto.getEmail());
        admin.setContactNumber(dto.getContactNumber());
        admin.setPasswordHash(hashedPassword);
        admin.setVerified(false);
        return admin;
    }
}
