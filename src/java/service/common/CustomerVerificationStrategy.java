// service.common.CustomerVerificationStrategy.java
package service.common;

import dao.CustomerDAO;

public class CustomerVerificationStrategy implements UserVerificationStrategy {

    private final CustomerDAO customerDAO;

    public CustomerVerificationStrategy(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public void verify(String userId) throws Exception {
        customerDAO.verify(userId);
    }
}