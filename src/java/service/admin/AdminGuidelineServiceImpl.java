package service.admin;

import dao.GuidelineDAO;
import model.Guideline;
import util.DAOExeption;
import util.IDGenerator;
import util.*;
import java.util.List;

import dto.GuidelineDTO;
import service.common.Validator; // Interface
import service.admin.GuidelineValidator; // Your implementation

public class AdminGuidelineServiceImpl implements AdminGuidelineService {

    private final GuidelineDAO guidelineDAO;
    private final Validator<GuidelineDTO> validator;

    public AdminGuidelineServiceImpl(GuidelineDAO guidelineDAO, Validator<GuidelineDTO> validator) {
        this.guidelineDAO = guidelineDAO;
        this.validator = validator;
    }

  @Override
public void createGuideline(String title, String content) throws DAOExeption {
    // Validate first
    GuidelineDTO dto = new GuidelineDTO(title, content);
    validator.validate(dto);

    // Create Guideline object
    Guideline guideline = new Guideline();

    // Use UUIDGenerator instance
    IDGenerator<String> idGen = new UUIDGenerator();
    guideline.setId(idGen.generate()); // returns String

    guideline.setTitle(dto.getTitle());
    guideline.setContent(dto.getContent());

    // Debug print
    System.out.println("Saving guideline to DB:");
    System.out.println("ID: " + guideline.getId());
    System.out.println("Title: " + guideline.getTitle());
    System.out.println("Content: " + guideline.getContent());

    // Save to DB
    guidelineDAO.save(guideline);
}


    @Override
    public void updateGuideline(String id, String title, String content) throws DAOExeption {
        GuidelineDTO dto = new GuidelineDTO(title, content);
        validator.validate(dto);

        // ... rest unchanged
    }

    // ... other methods unchanged


    @Override
    public void deleteGuideline(String id) throws DAOExeption {
        guidelineDAO.delete(id);
    }

    @Override
    public List<Guideline> getAllGuidelines() throws DAOExeption {
        return guidelineDAO.findAll();
    }

    @Override
    public Guideline getById(String id) throws DAOExeption {
        return guidelineDAO.findById(id);
    }
}
