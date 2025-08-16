package service.admin;

import model.Guideline;
import util.DAOExeption;

import java.util.List;

public interface AdminGuidelineService {
    void createGuideline(String title, String content) throws DAOExeption;
    void updateGuideline(String id, String title, String content) throws DAOExeption;
    void deleteGuideline(String id) throws DAOExeption;
    List<Guideline> getAllGuidelines() throws DAOExeption;
    Guideline getById(String id) throws DAOExeption;
}
