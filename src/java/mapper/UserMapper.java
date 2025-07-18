package mapper;

import dto.CustomerDTO;
import dto.AdminDTO;
import dto.StaffDTO;
import model.Customer;
import model.Admin;
import model.Staff;
import model.*;
import dto.*;
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
      
    
    public static DeliveryPartner toDeliveryPartner(DeliveryPartnerDTO dto, String id, String passwordHash) {
    DeliveryPartner dp = new DeliveryPartner();
    dp.setId(id);
    dp.setUsername(dto.getUsername());
    dp.setFirstName(dto.getFirstName());
    dp.setLastName(dto.getLastName());
    dp.setEmail(dto.getEmail());
    dp.setContactNumber(dto.getContactNumber());
    dp.setPasswordHash(passwordHash);
    dp.setVehicleNumber(dto.getVehicleNumber());
    dp.setVerified(false);
    dp.setStatus("PENDING");
    return dp;
}
    
    public static DeliveryPartnerDTO toDeliveryPartnerDTO(DeliveryPartner dp) {
    DeliveryPartnerDTO dto = new DeliveryPartnerDTO();
    dto.setId(dp.getId());
    dto.setUsername(dp.getUsername());
    dto.setFirstName(dp.getFirstName());
    dto.setLastName(dp.getLastName());
    dto.setEmail(dp.getEmail());
    dto.setContactNumber(dp.getContactNumber());
    dto.setVehicleNumber(dp.getVehicleNumber());
    return dto;
}



}
//user mapper class implemented