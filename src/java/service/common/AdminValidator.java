// service/common/AdminValidator.java
package service.common;

import dto.AdminDTO;
import util.ValidationException;

public class AdminValidator implements Validator<AdminDTO> {

    @Override
    public void validate(AdminDTO dto) throws ValidationException {
        validateCommon(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getConfirmPassword());
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
