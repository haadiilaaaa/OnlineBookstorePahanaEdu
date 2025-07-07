package util;

import dto.AdminDTO;
import dto.CustomerDTO;
import dto.StaffDTO;

import javax.servlet.http.HttpServletRequest;

public class RegistrationRequestBuilder {

    public static Object buildDTO(String userType, HttpServletRequest req) {
        return switch (userType) {
            case "customer" -> {
                CustomerDTO dto = new CustomerDTO();
                dto.setUsername(req.getParameter("username"));
                dto.setFirstName(req.getParameter("first_name"));
                dto.setLastName(req.getParameter("last_name"));
                dto.setEmail(req.getParameter("email"));
                dto.setContactNumber(req.getParameter("contact_number"));
                dto.setAddress(req.getParameter("address"));
                dto.setPassword(req.getParameter("password_hash"));
                dto.setConfirmPassword(req.getParameter("confirm_password")); // ✅ important
                yield dto;
            }
            case "admin" -> {
                AdminDTO dto = new AdminDTO();
                dto.setUsername(req.getParameter("username"));
                dto.setFirstName(req.getParameter("first_name"));
                dto.setLastName(req.getParameter("last_name"));
                dto.setEmail(req.getParameter("email"));
                dto.setContactNumber(req.getParameter("contact_number"));
                dto.setPassword(req.getParameter("password_hash"));
                dto.setConfirmPassword(req.getParameter("confirm_password")); // ✅ important
                yield dto;
            }
            case "staff" -> {
                StaffDTO dto = new StaffDTO();
                dto.setUsername(req.getParameter("username"));
                dto.setFirstName(req.getParameter("first_name"));
                dto.setLastName(req.getParameter("last_name"));
                dto.setEmail(req.getParameter("email"));
                dto.setContactNumber(req.getParameter("contact_number"));
                dto.setPassword(req.getParameter("password_hash"));
                dto.setConfirmPassword(req.getParameter("confirm_password")); // ✅ important
                yield dto;
            }
            default -> throw new IllegalArgumentException("Invalid user type: " + userType);
        };
    }
}
