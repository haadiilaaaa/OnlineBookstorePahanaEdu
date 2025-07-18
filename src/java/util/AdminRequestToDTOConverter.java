package util;

import dto.AdminDTO;
import javax.servlet.http.HttpServletRequest;

public class AdminRequestToDTOConverter implements RequestToDTOConverter {

    @Override
    public Object convert(HttpServletRequest req) {
        AdminDTO dto = new AdminDTO();
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
