package util;

import dto.StaffDTO;
import javax.servlet.http.HttpServletRequest;

public class StaffRequestToDTOConverter implements RequestToDTOConverter {

    @Override
    public Object convert(HttpServletRequest req) {
        StaffDTO dto = new StaffDTO();
        dto.setUsername(req.getParameter("username"));
        dto.setFirstName(req.getParameter("first_name"));
        dto.setLastName(req.getParameter("last_name"));
        dto.setEmail(req.getParameter("email"));
        dto.setContactNumber(req.getParameter("contact_number"));
        dto.setPassword(req.getParameter("password_hash"));
        dto.setConfirmPassword(req.getParameter("confirm_password"));
        return dto;
    }
}
