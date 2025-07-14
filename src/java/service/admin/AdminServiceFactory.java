package service.admin;

import dao.*;
import mapper.*;
import service.admin.*;
import service.customer.*;
import java.sql.Connection;

public class AdminServiceFactory {

   public static ItemService createItemService(Connection conn) {
    CategoryDAO categoryDAO = new CategoryDAOImpl(conn); // ✅ first create categoryDAO
    ItemDAO itemDAO = new ItemDAOImpl(conn); // ✅ pass both connection & categoryDAO

    DiscountDAO discountDAO = new DicountDAOimpl(conn);
    DiscountAssignmentDAO discountAssignmentDAO = new DiscountAssignmentDAOImpl(conn);

    DiscountService discountService = new DiscountServiceImpl(discountAssignmentDAO, discountDAO);

    return new ItemServiceImpl(
        itemDAO,
        categoryDAO,
        new ItemMapper(),
        discountService
    );
}


    public static CategoryService createCategoryService(Connection conn) {
        return new CategoryServiceImpl(
            new CategoryDAOImpl(conn),
            new CategoryMapper()
        );
    }
}
