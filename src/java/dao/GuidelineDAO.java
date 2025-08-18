package dao;

import model.Guideline;
import util.DAOExeption;

import java.util.List;

public interface GuidelineDAO {   
    void save(Guideline guideline) throws DAOExeption;
    void update(Guideline guideline) throws DAOExeption;
    void delete(String id) throws DAOExeption;
    List<Guideline> findAll() throws DAOExeption;
    Guideline findById(String id) throws DAOExeption;
    

    List<String> findAllIds() throws DAOExeption;
}


