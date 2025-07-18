package service.deliveryPartner;

import dto.DeliveryPartnerDTO;

public interface DeliveryPartnerRegistrationService {
    void register(DeliveryPartnerDTO dto) throws Exception;
}
