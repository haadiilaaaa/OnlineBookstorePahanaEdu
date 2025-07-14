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

public class RegistrationFacadeServiceImpl implements RegistrationFacadeService {

    private interface RegistrationStrategy {
        void register(Object dto) throws Exception;
    }

    private final Map<String, RegistrationStrategy> strategies = new HashMap<>();

    public RegistrationFacadeServiceImpl(RegisterCustomerService customerService,
                                         RegisterServiceAdmin adminService,
                                         RegisterStaffService staffService) {
        strategies.put("customer", dto -> customerService.register((CustomerDTO) dto));
        strategies.put("admin", dto -> adminService.register((AdminDTO) dto));
        strategies.put("staff", dto -> staffService.register((StaffDTO) dto));
    }

    @Override
    public String register(String userType, HttpServletRequest request) throws Exception {
        RegistrationStrategy strategy = strategies.get(userType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown user type: " + userType);
        }

        // Build DTO from request based on userType
        Object dto = RegistrationRequestBuilder.buildDTO(userType, request);

        // Register using the appropriate strategy
        strategy.register(dto);

        // Extract the user ID after registration
        return UserIdExtractor.extractId(userType, dto);
    }
}
