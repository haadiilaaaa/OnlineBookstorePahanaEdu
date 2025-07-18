// service.common.DeliveryPartnerVerificationStrategy.java
package service.common;

import dao.DeliveryPartnerDAO;

public class DeliveryPartnerVerificationStrategy implements UserVerificationStrategy {

    private final DeliveryPartnerDAO deliveryPartnerDAO;

    public DeliveryPartnerVerificationStrategy(DeliveryPartnerDAO deliveryPartnerDAO) {
        this.deliveryPartnerDAO = deliveryPartnerDAO;
    }

    @Override
    public void verify(String userId) throws Exception {
        deliveryPartnerDAO.verify(userId);
    }
}
