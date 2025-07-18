// service/common/InputValidationServiceImpl.java
package service.common;

import util.ValidationException;

public class InputValidationServiceImpl implements InputValidationService {

    @Override
    public void validate(Object dto) throws ValidationException {
        String userType = determineUserType(dto);
        Validator validator = ValidatorFactory.getValidator(userType);
        validator.validate(dto);
    }

    private String determineUserType(Object dto) throws ValidationException {
    if (dto instanceof dto.CustomerDTO) return "customer";
    else if (dto instanceof dto.AdminDTO) return "admin";
    else if (dto instanceof dto.StaffDTO) return "staff";
    else if (dto instanceof dto.DeliveryPartnerDTO) return "delivery"; // <-- Add this line
    else throw new ValidationException("Unsupported DTO type for validation.");
}

}
