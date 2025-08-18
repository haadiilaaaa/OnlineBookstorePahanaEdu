package dao;

import model.Discount;
import java.util.List;

public interface DiscountDAO {  
    void save(Discount discount) throws Exception;
    List<Discount> findAll() throws Exception;
    Discount findById(String id) throws Exception;
    int getMaxDiscountIdNumber() throws Exception;
    List<Discount> findActiveDiscounts() throws Exception;

}