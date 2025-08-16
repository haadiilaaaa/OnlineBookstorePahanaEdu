package dao;
import java.util.List;
import dto.DeliveryPartnerDTO;
import model.DeliveryPartner;
import util.DAOExeption;
import java.util.Optional;

public interface DeliveryPartnerDAO extends GenericUserDAO<DeliveryPartner>, PasswordUpdatabale {
    void save(DeliveryPartner deliveryPartner) throws Exception;
    int countPartners() throws Exception;
    void verify(String userId) throws Exception;
    void updatePassword(String userId, String newPasswordHash) throws Exception;
     List<DeliveryPartnerDTO> findByStatus(String status) throws Exception;
      boolean updateStatus(String userId, String newStatus) throws DAOExeption;
      Optional<DeliveryPartner> findByUsernameOrEmail(String usernameOrEmail) throws DAOExeption;
      List<DeliveryPartnerDTO> getAllPartners() throws DAOExeption;
      void update(DeliveryPartner dp) throws Exception;
      List<DeliveryPartner> findAll() throws Exception;
      


}
