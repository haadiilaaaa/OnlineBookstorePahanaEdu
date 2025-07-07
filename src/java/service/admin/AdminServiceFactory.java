package service.admin;

import dao.*;
import db.DBConnection;
import mapper.*;
import service.admin.*;

import java.sql.Connection;

public class AdminServiceFactory {

    public static ItemService createItemService(Connection conn) {
        return new ItemServiceImpl(
            new ItemDAOImpl(conn),
            new CategoryDAOImpl(conn),
            new ItemMapper(),
            new DicountDAOimpl(conn),
            new DiscountAssignmentDAOImpl(conn)        
        );
    }

    public static CategoryService createCategoryService(Connection conn) {
        return new CategoryServiceImpl(
            new CategoryDAOImpl(conn),
            new CategoryMapper()
        );
    }
}
