package service.admin;

import dto.GuidelineDTO;
import service.common.Validator;
import util.ValidationException;

public class GuidelineValidator implements Validator<GuidelineDTO> {

    @Override
    public void validate(GuidelineDTO dto) throws ValidationException {
        if (dto == null) {
            throw new ValidationException("Guideline data cannot be null");
        }
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new ValidationException("Title is required");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new ValidationException("Content is required");
        }
    }
}
