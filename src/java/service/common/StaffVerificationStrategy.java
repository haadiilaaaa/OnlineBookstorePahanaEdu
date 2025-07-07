// service.common.StaffVerificationStrategy.java
package service.common;

import dao.StaffDAO;

public class StaffVerificationStrategy implements UserVerificationStrategy {
    private final StaffDAO staffDAO;

    public StaffVerificationStrategy(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }

    @Override
    public void verify(String userId) throws Exception {
        staffDAO.verify(userId);
    }
}
