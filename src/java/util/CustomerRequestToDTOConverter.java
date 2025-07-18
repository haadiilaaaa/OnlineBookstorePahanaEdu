package util;

import dto.CustomerDTO;
import javax.servlet.http.HttpServletRequest;

public class CustomerRequestToDTOConverter implements RequestToDTOConverter {

    @Override
    public Object convert(HttpServletRequest req) {
        CustomerDTO dto = new CustomerDTO();
        dto.setUsername(req.getParameter("username"));
        dto.setFirstName(req.getParameter("first_name"));
        dto.setLastName(req.getParameter("last_name"));
        dto.setEmail(req.getParameter("email"));
        dto.setContactNumber(req.getParameter("contact_number"));
        dto.setAddress(req.getParameter("address"));
        dto.setPassword(req.getParameter("password_hash"));
        dto.setConfirmPassword(req.getParameter("confirm_password"));
        return dto;
    }
}
