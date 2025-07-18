package util;

import dto.DeliveryPartnerDTO;

import javax.servlet.http.HttpServletRequest;

public class DeliveryRequestToDTOConverter implements RequestToDTOConverter {

    @Override
    public DeliveryPartnerDTO convert(HttpServletRequest request) {
        DeliveryPartnerDTO dto = new DeliveryPartnerDTO();
        System.out.println("DeliveryRequestToDTOConverter: created DTO: " + dto);
        dto.setUsername(request.getParameter("username"));
        dto.setFirstName(request.getParameter("firstName"));
        dto.setLastName(request.getParameter("lastName"));
        dto.setEmail(request.getParameter("email"));
        dto.setContactNumber(request.getParameter("contactNumber"));
        dto.setPassword(request.getParameter("password"));
        dto.setConfirmPassword(request.getParameter("confirmPassword"));
        dto.setVehicleNumber(request.getParameter("vehicleNumber"));
        return dto;
    }
}
