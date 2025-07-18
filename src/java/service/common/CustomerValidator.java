package service.common;

import dto.CustomerDTO;
import util.ValidationException;

public class CustomerValidator implements Validator<CustomerDTO> {

    // Validation used during registration (password required)
    @Override
    public void validate(CustomerDTO dto) throws ValidationException {
        validateCommon(dto.getUsername(), dto.getEmail());
        validatePassword(dto.getPassword(), dto.getConfirmPassword());
        validateAddress(dto.getAddress());
    }

    // Validation used during profile editing (password optional)
    public void validateForEdit(CustomerDTO dto) throws ValidationException {
        validateCommon(dto.getUsername(), dto.getEmail());
        validateAddress(dto.getAddress());
        
        // Only validate password if it is provided (not blank)
        if (isPasswordProvided(dto.getPassword())) {
            validatePassword(dto.getPassword(), dto.getConfirmPassword());
        }
    }

    private void validateCommon(String username, String email) throws ValidationException {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required.");
        }
        if (email == null || !email.matches("^.+@.+\\..+$")) {
            throw new ValidationException("Invalid email address.");
        }
    }

    private void validatePassword(String password, String confirmPassword) throws ValidationException {
        if (password == null || password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters.");
        }
        if (!password.equals(confirmPassword)) {
            throw new ValidationException("Passwords do not match.");
        }
    }

    private void validateAddress(String address) throws ValidationException {
        if (address == null || address.isBlank()) {
            throw new ValidationException("Address is required for customers.");
        }
    }

    // Utility method to check if password field is non-empty
    private boolean isPasswordProvided(String password) {
        return password != null && !password.isBlank();
    }
}
