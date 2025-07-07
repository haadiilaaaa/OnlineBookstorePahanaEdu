package strategy;

import dto.AdminDTO;
import service.admin.RegisterServiceAdmin;

public class AdminStrategy implements RegistrationStrategy {

    private final RegisterServiceAdmin registerService;

    public AdminStrategy(RegisterServiceAdmin registerService) {
        this.registerService = registerService;
    }

    @Override
    public void register(Object dto) throws Exception {
        if (!(dto instanceof AdminDTO adminDTO)) {
            throw new IllegalArgumentException("Invalid DTO type for AdminStrategy");
        }

        registerService.register(adminDTO);
    }
}
