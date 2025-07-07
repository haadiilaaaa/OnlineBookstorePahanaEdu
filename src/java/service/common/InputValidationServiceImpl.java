package service.common;

import dto.CustomerDTO;
import dto.AdminDTO;
import dto.StaffDTO;
import util.ValidationException;

public class InputValidationServiceImpl implements InputValidationService {

    @Override
    public void validate(Object dto) throws ValidationException {
        if (dto instanceof CustomerDTO customer) {
            validateCommon(customer.getUsername(), customer.getEmail(), customer.getPassword(), customer.getConfirmPassword());

            if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
                throw new ValidationException("Address is required for customers.");
            }

        } else if (dto instanceof AdminDTO admin) {
            validateCommon(admin.getUsername(), admin.getEmail(), admin.getPassword(), admin.getConfirmPassword());

        } else if (dto instanceof StaffDTO staff) {
            validateCommon(staff.getUsername(), staff.getEmail(), staff.getPassword(), staff.getConfirmPassword());

        } else {
            throw new ValidationException("Unsupported DTO type for validation.");
        }
    }

    private void validateCommon(String username, String email, String password, String confirmPassword) throws ValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username is required.");
        }

        if (email == null || !email.matches("^.+@.+\\..+$")) {
            throw new ValidationException("Invalid email address.");
        }

        if (password == null || password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters.");
        }

        if (!password.equals(confirmPassword)) {
            throw new ValidationException("Passwords do not match.");
        }
    }
}
