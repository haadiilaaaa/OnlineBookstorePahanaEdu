package service.common;

import dto.CustomerDTO;
import dto.AdminDTO;
import dto.StaffDTO;

public class InputValidationServiceImpl implements InputValidationService {

    @Override
    public void validate(Object dto) throws Exception {
        if (dto instanceof CustomerDTO customer) {
            validateCommon(customer.getUsername(), customer.getEmail(), customer.getPassword());
            if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
                throw new Exception("Address is required for customers.");
            }

        } else if (dto instanceof AdminDTO admin) {
            validateCommon(admin.getUsername(), admin.getEmail(), admin.getPassword());

        } else if (dto instanceof StaffDTO staff) {
            validateCommon(staff.getUsername(), staff.getEmail(), staff.getPassword());

        } else {
            throw new IllegalArgumentException("Unsupported DTO type for validation.");
        }
    }

    private void validateCommon(String username, String email, String password) throws Exception {
        if (username == null || username.isEmpty()) {
            throw new Exception("Username is required.");
        }
        if (email == null || !email.matches("^.+@.+\\..+$")) {
            throw new Exception("Invalid email address.");
        }
        if (password == null || password.length() < 6) {
            throw new Exception("Password must be at least 6 characters.");
        }
    }
}
