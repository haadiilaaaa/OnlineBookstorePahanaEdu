package service.admin;

import dao.GuidelineDAO;
import model.Guideline;
import util.DAOExeption;

import java.util.List;
import java.util.UUID;

public class AdminGuidelineServiceImpl implements AdminGuidelineService {

    private final GuidelineDAO guidelineDAO;

    public AdminGuidelineServiceImpl(GuidelineDAO guidelineDAO) {
        this.guidelineDAO = guidelineDAO;
    }
@Override
public void createGuideline(String title, String content) throws DAOExeption {
    Guideline g = new Guideline();

    // Assign the smallest available ID (like G001, G002, etc.)
    g.setId(getNextAvailableGuidelineId());

    g.setTitle(title);
    g.setContent(content);
    guidelineDAO.save(g);
}


    @Override
    public void updateGuideline(String id, String title, String content) throws DAOExeption {
        Guideline g = new Guideline();
        g.setId(id);
        g.setTitle(title);
        g.setContent(content);
        guidelineDAO.update(g);
    }

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
    
    private String getNextAvailableGuidelineId() throws DAOExeption {
    List<String> existingIds = guidelineDAO.findAllIds(); // get all current IDs

    // If no IDs exist yet, return the first one
    if (existingIds.isEmpty()) {
        return "G001";
    }

    // Iterate from 1 up to existingIds.size() + 1 to find the missing number
    for (int i = 1; i <= existingIds.size() + 1; i++) {
        String candidateId = String.format("G%03d", i);
        if (!existingIds.contains(candidateId)) {
            return candidateId;
        }
    }

    // Fallback - should never get here
    return String.format("G%03d", existingIds.size() + 1);
}

}
