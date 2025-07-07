package service.admin;

import dto.AdminDashboardDTO;

public interface AdminDashoardService {
    AdminDashboardDTO loadDashboard(String adminId) throws Exception;
    
}
