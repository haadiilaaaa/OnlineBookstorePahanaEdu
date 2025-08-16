package service.admin;

import dao.*;
import mapper.*;
import service.admin.*;
import service.customer.*;
import util.IDGenerator;
import util.NextSequentialIDGenerator;

import java.sql.Connection;
import java.util.Collections;

public class AdminServiceFactory {

    public static ItemService createItemService(Connection conn) {
        CategoryDAO categoryDAO = new CategoryDAOImpl(conn);
        ItemDAO itemDAO = new ItemDAOImpl(conn);

        // Create a dummy IDGenerator with an empty list of IDs, since this factory's
        // purpose is likely not to create new discounts.
        IDGenerator<String> dummyDiscountIdGenerator = new NextSequentialIDGenerator("dis", Collections.emptyList());
        
        // Pass both the connection and the dummy IDGenerator to the constructor
        DiscountDAO discountDAO = new DicountDAOimpl(conn, dummyDiscountIdGenerator);
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