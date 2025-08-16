package service.admin;

import dao.GuidelineDAO;
import dao.GuidelineDAOImpl;
import db.DBConnection;
import dto.GuidelineDTO;
import service.admin.GuidelineValidator;
import service.common.Validator;

import java.sql.Connection;
import java.sql.SQLException;

public class AdminGuidelineServiceProvider {
    public static AdminGuidelineService getService() throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        GuidelineDAO guidelineDAO = new GuidelineDAOImpl(conn);
        Validator<GuidelineDTO> validator = new GuidelineValidator();

        return new AdminGuidelineServiceImpl(guidelineDAO, validator);
    }
}
