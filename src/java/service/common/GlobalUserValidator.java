package service.common;

import dao.AdminDAO;
import dao.CustomerDAO;
import dao.StaffDAO;
import util.*;

public class GlobalUserValidator {
//
    private final CustomerDAO customerDAO;
    private final AdminDAO adminDAO;
    private final StaffDAO staffDAO;

    public GlobalUserValidator(CustomerDAO customerDAO, AdminDAO adminDAO, StaffDAO staffDAO) {
        this.customerDAO = customerDAO;
        this.adminDAO = adminDAO;
        this.staffDAO = staffDAO;
    }

    public void validateUniqueUsernameAndEmail(String username, String email) throws Exception {
        System.out.println("Checking uniquesness for:"+username+", "+email);
        if (isUsernameTaken(username)) {
            throw new ValidationException("Username is already taken.");
        }
        if (isEmailTaken(email)) {
            throw new ValidationException("Email is already registered.");
        }
    }

    private boolean isUsernameTaken(String username) throws Exception {
        return customerDAO.findByUsername(username) != null ||
               adminDAO.findByUsername(username) != null ||
               staffDAO.findByUsername(username) != null;
    }

    private boolean isEmailTaken(String email) throws Exception {
        return customerDAO.findByEmail(email) != null ||
               adminDAO.findByEmail(email) != null ||
               staffDAO.findByEmail(email) != null;
    }
}
