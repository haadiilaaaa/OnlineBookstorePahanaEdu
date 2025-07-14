// service/common/CustomerValidator.java
package service.common;

import dto.CustomerDTO;
import util.ValidationException;

public class CustomerValidator implements Validator<CustomerDTO> {

    @Override
    public void validate(CustomerDTO dto) throws ValidationException {
        validateCommon(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getConfirmPassword());
        if (dto.getAddress() == null || dto.getAddress().trim().isEmpty()) {
            throw new ValidationException("Address is required for customers.");
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
