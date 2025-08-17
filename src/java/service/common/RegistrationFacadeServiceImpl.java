package service.common;

import dto.CustomerDTO;
import dto.AdminDTO;
import dto.StaffDTO;
import service.customer.RegisterCustomerService;
import service.admin.RegisterServiceAdmin;
import service.staff.RegisterStaffService;
import util.RegistrationRequestBuilder;
import util.UserIdExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;  
import java.util.Map;  
 import dto.DeliveryPartnerDTO;
import service.deliveryPartner.DeliveryPartnerRegistrationService;

public class RegistrationFacadeServiceImpl implements RegistrationFacadeService {

    private interface RegistrationStrategy {
        void register(Object dto) throws Exception;
    }

    private final Map<String, RegistrationStrategy> strategies = new HashMap<>();

   

public RegistrationFacadeServiceImpl(RegisterCustomerService customerService,
                                     RegisterServiceAdmin adminService,
                                     RegisterStaffService staffService,
                                     DeliveryPartnerRegistrationService deliveryService) {
    strategies.put("customer", dto -> customerService.register((CustomerDTO) dto));
    strategies.put("admin", dto -> adminService.register((AdminDTO) dto));
    strategies.put("staff", dto -> staffService.register((StaffDTO) dto));
    strategies.put("delivery", dto -> deliveryService.register((DeliveryPartnerDTO) dto)); // ✅ Add this
}

  @Override
public String register(String userType, HttpServletRequest request) throws Exception {
    RegistrationStrategy strategy = strategies.get(userType);
    if (strategy == null) {
        throw new IllegalArgumentException("Unknown user type: " + userType);
    }

    // 1. Build DTO
    Object dto = RegistrationRequestBuilder.buildDTO(userType, request);

    // 2. Validate DTO with ValidatorFactory
    Validator validator = ValidatorFactory.getValidator(userType);
validator.validate(dto); // if Validator<?> is used in the factory
  // ✅ Fix is here
    System.out.println("Before validation: DTO class=" + dto.getClass().getName() + ", validator class=" + validator.getClass().getName());
    validator.validate(dto);  // ✅ Now this works safely
    System.out.println("Validation succeeded");

    // 3. Register user using strategy
    strategy.register(dto);

    // 4. Extract and return the ID
    return UserIdExtractor.extractId(userType, dto);
}

}
