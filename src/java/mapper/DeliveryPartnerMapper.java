package mapper;

import model.DeliveryPartner;
import dto.DeliveryPartnerDTO;

public class DeliveryPartnerMapper {

    public static DeliveryPartnerDTO toDTO(DeliveryPartner model) {
        DeliveryPartnerDTO dto = new DeliveryPartnerDTO();
        dto.setId(model.getId());
        dto.setUsername(model.getUsername());
        dto.setFirstName(model.getFirstName());
        dto.setLastName(model.getLastName());
        dto.setEmail(model.getEmail());
        dto.setContactNumber(model.getContactNumber());
        dto.setVehicleNumber(model.getVehicleNumber());
        dto.setStatus(model.getStatus());
        return dto;
    }
}
