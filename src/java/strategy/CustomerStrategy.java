package strategy;

import dto.CustomerDTO;
import service.customer.RegisterCustomerService;

public class CustomerStrategy implements RegistrationStrategy {

    private final RegisterCustomerService registerService;

    public CustomerStrategy(RegisterCustomerService registerService) {
        this.registerService = registerService;
    }

    @Override
    public void register(Object dto) throws Exception {
        if (!(dto instanceof CustomerDTO customerDTO)) {
            throw new IllegalArgumentException("Invalid DTO type for CustomerStrategy");
        }

        registerService.register(customerDTO); // Delegates the actual logic
    }
}
