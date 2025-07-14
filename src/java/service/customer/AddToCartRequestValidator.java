package service.customer;

import dto.AddToCartRequestDTO;
import service.common.Validator;
import util.ValidationException;

public class AddToCartRequestValidator implements Validator<AddToCartRequestDTO> {

    @Override
    public void validate(AddToCartRequestDTO dto) throws ValidationException {
        if (dto.getItemId() == null || dto.getItemId().trim().isEmpty()) {
            throw new ValidationException("Item ID is required");
        }
        String quantityStr = dto.getQuantity();
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            throw new ValidationException("Quantity is required");
        }
        try {
            int qty = Integer.parseInt(quantityStr);
            if (qty <= 0) {
                throw new ValidationException("Quantity must be a positive number");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Quantity must be a valid number");
        }
    }
}
