package service.common;

import dto.DeliveryPartnerDTO;
import util.ValidationException;

public class DeliveryPartnerValidator implements Validator<DeliveryPartnerDTO> {
    
    

   @Override
public void validate(DeliveryPartnerDTO dto) throws ValidationException {
    
    System.out.println("Validating DTO of type: " + dto.getClass().getName());

    // No instanceof check needed here

    if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
        throw new ValidationException("Username is required.");
    }
    if (dto.getEmail() == null || !dto.getEmail().matches("^.+@.+\\..+$")) {
        throw new ValidationException("Invalid email address.");
    }
    if (dto.getPassword() == null || dto.getPassword().length() < 6) {
        throw new ValidationException("Password must be at least 6 characters.");
    }
    if (!dto.getPassword().equals(dto.getConfirmPassword())) {
        throw new ValidationException("Passwords do not match.");
    }
    if (dto.getVehicleNumber() == null || dto.getVehicleNumber().trim().isEmpty()) {
        throw new ValidationException("Vehicle number is required.");
    }
}

}
