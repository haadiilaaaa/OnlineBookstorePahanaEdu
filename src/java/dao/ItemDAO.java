package dao;

import model.Item;
import java.util.List;
import java.math.BigDecimal;

public interface ItemDAO {  
    void save(Item item) throws Exception;
    List<Item> findAll() throws Exception;
       
    int getItemCount() throws Exception;
    void update(Item item) throws Exception;
    void delete(String id) throws Exception;
    Item findById(String id) throws Exception;
    
    List<Item> findByCategoryId(String categoryId) throws Exception;
  List<Item> advancedSearch(String keyword, String categoryId, BigDecimal minPrice, BigDecimal maxPrice) throws Exception;


    
}
