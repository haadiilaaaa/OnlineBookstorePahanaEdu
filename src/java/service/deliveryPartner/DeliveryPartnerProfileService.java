package service.deliveryPartner;
import java.util.List;
import dto.DeliveryPartnerDTO;
import java.math.BigDecimal;

public interface DeliveryPartnerProfileService {
    DeliveryPartnerDTO getProfile(String id) throws Exception;
    void updateProfile(DeliveryPartnerDTO dto) throws Exception;
    BigDecimal getTotalEarnings(String partnerId) throws Exception;
    int getTotalDeliveries(String partnerId) throws Exception;
    int getPendingDeliveries(String partnerId) throws Exception;  // NEW
    List<String> getNotifications(String partnerId) throws Exception; // NEW, example notifications as strings

}
