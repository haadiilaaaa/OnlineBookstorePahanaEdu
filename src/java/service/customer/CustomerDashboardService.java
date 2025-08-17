package service.customer;
import model.Category;
import java.util.List;
import dto.CustomerDashboardDTO;

public interface CustomerDashboardService {
     CustomerDashboardDTO loadDashboard(String customerId) throws Exception;
    List<Category> getAllCategories() throws Exception;
}  
