package dto;

public class AdminDashboardDTO {

    private int totalItems;
    private int totalCategories;
    private int totalCustomers;
    private int totalStaff;
    private int totalActiveDiscounts;
    private String adminName;

    public AdminDashboardDTO(int totalItems, int totalCategories, int totalCustomers, int totalStaff, int totalActiveDiscounts,String adminName) {
        this.totalItems = totalItems;
        this.totalCategories = totalCategories;
        this.totalCustomers = totalCustomers;
        this.totalStaff = totalStaff;
        this.totalActiveDiscounts = totalActiveDiscounts;
        this.adminName = adminName;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalCategories() {
        return totalCategories;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public int getTotalStaff() {
        return totalStaff;
    }

    public int getTotalActiveDiscounts() {
        return totalActiveDiscounts;
    }
    public String getAdminName() {
    return adminName;
}
}
