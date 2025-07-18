package strategy;

import dto.DeliveryPartnerDTO;
import service.deliveryPartner.DeliveryPartnerRegistrationService;

public class DeliveryStrategy implements RegistrationStrategy {

    private final DeliveryPartnerRegistrationService registerService;

    public DeliveryStrategy(DeliveryPartnerRegistrationService registerService) {
        this.registerService = registerService;
    }

    @Override
    public void register(Object dto) throws Exception {
        if (!(dto instanceof DeliveryPartnerDTO deliveryPartnerDTO)) {
            throw new IllegalArgumentException("Invalid DTO type for DeliveryPartnerStrategy");
        }

        registerService.register(deliveryPartnerDTO); // Delegate actual registration logic
    }
}
