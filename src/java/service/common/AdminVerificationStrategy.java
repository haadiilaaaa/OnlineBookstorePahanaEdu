// service.common.AdminVerificationStrategy.java
package service.common;

import dao.AdminDAO;

public class AdminVerificationStrategy implements UserVerificationStrategy {
    private final AdminDAO adminDAO;

    public AdminVerificationStrategy(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @Override
    public void verify(String userId) throws Exception {
        adminDAO.verify(userId);
    }
}
