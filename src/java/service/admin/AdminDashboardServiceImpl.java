package service.admin;

import dao.*;
import dto.AdminDashboardDTO;
import model.Discount;

import java.util.List;

public class AdminDashboardServiceImpl implements AdminDashoardService {

    private final CustomerDAO customerDAO;
    private final StaffDAO staffDAO;
    private final ItemDAO itemDAO;
    private final CategoryDAO categoryDAO;
    private final DiscountDAO discountDAO;
    private final AdminDAO adminDAO;

    public AdminDashboardServiceImpl(CustomerDAO customerDAO, StaffDAO staffDAO, ItemDAO itemDAO,
                                     CategoryDAO categoryDAO, DiscountDAO discountDAO,AdminDAO adminDAO) {
        this.customerDAO = customerDAO;
        this.staffDAO = staffDAO;
        this.itemDAO = itemDAO;
        this.categoryDAO = categoryDAO;
        this.discountDAO = discountDAO;
        this.adminDAO = adminDAO;
    }

    public AdminDashboardDTO loadDashboard(String adminId) throws Exception {
    int totalItems = itemDAO.getItemCount();
    int totalCategories = categoryDAO.getCategoryCount();
    int totalCustomers = customerDAO.countCustomers();
    int totalStaff = staffDAO.countStaff();
    int totalActiveDiscounts = discountDAO.findActiveDiscounts().size();


    String adminName = "Admin"; // fallback
    model.Admin admin = adminDAO.findById(adminId);
    if (admin != null) {
        adminName = admin.getFirstName() + " " + admin.getLastName();
    }

    return new AdminDashboardDTO(
        totalItems,
        totalCategories,
        totalCustomers,
        totalStaff,
        totalActiveDiscounts,
        adminName
    );
}

}
