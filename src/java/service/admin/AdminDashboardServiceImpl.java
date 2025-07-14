package service.admin;

import dao.*;
import dto.AdminDashboardDTO;
import model.Admin;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of AdminDashboardService.
 * Responsible for assembling dashboard data for Admin user.
 */
public class AdminDashboardServiceImpl implements AdminDashoardService {

    private static final Logger logger = Logger.getLogger(AdminDashboardServiceImpl.class.getName());

    private final CustomerDAO customerDAO;
    private final StaffDAO staffDAO;
    private final ItemDAO itemDAO;
    private final CategoryDAO categoryDAO;
    private final DiscountDAO discountDAO;
    private final AdminDAO adminDAO;

    public AdminDashboardServiceImpl(CustomerDAO customerDAO, StaffDAO staffDAO, ItemDAO itemDAO,
                                     CategoryDAO categoryDAO, DiscountDAO discountDAO, AdminDAO adminDAO) {
        this.customerDAO = customerDAO;
        this.staffDAO = staffDAO;
        this.itemDAO = itemDAO;
        this.categoryDAO = categoryDAO;
        this.discountDAO = discountDAO;
        this.adminDAO = adminDAO;
    }

    /**
     * Loads the dashboard data for the admin with the given ID.
     *
     * @param adminId The ID of the admin.
     * @return DTO containing dashboard information.
     * @throws ServiceException Wraps underlying DAO exceptions.
     */
    @Override
    public AdminDashboardDTO loadDashboard(String adminId) throws ServiceException {
        try {
            logger.info("Loading admin dashboard for ID: " + adminId);

            final int totalItems = itemDAO.getItemCount();
            final int totalCategories = categoryDAO.getCategoryCount();
            final int totalCustomers = customerDAO.countCustomers();
            final int totalStaff = staffDAO.countStaff();
            final int totalActiveDiscounts = discountDAO.findActiveDiscounts().size();
            final String adminName = getAdminFullName(adminId);

            logger.info("Dashboard data loaded successfully for admin: " + adminName);

            return new AdminDashboardDTO(
                    totalItems,
                    totalCategories,
                    totalCustomers,
                    totalStaff,
                    totalActiveDiscounts,
                    adminName
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading dashboard for admin ID: " + adminId, e);
            throw new ServiceException("Failed to load admin dashboard", e);
        }
    }

    /**
     * Retrieves full name of admin by ID, or returns fallback name.
     *
     * @param adminId The admin's ID.
     * @return Full name or fallback string.
     * @throws Exception Propagated from DAO layer.
     */
    private String getAdminFullName(String adminId) throws Exception {
        Optional<Admin> optionalAdmin = adminDAO.findById(adminId);
        return optionalAdmin
                .map(admin -> admin.getFirstName() + " " + admin.getLastName())
                .orElse("Admin");
    }
}
