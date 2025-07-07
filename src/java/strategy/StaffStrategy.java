package strategy;

import dto.StaffDTO;
import service.staff.RegisterStaffService;

public class StaffStrategy implements RegistrationStrategy {

    private final RegisterStaffService registerService;

    public StaffStrategy(RegisterStaffService registerService) {
        this.registerService = registerService;
    }

    @Override
    public void register(Object dto) throws Exception {
        if (!(dto instanceof StaffDTO staffDTO)) {
            throw new IllegalArgumentException("Invalid DTO type for StaffStrategy");
        }

        registerService.register(staffDTO);
    }
}
