   package service.customer;

import dao.*;
import mapper.*;
import service.admin.*;
import service.customer.*;
import service.common.*;
import javax.servlet.ServletContext;
import java.sql.Connection;

public class ItemServiceFactory {

  public static ItemService createItemService(Connection conn) {
    DiscountDAO discountDAO = new DicountDAOimpl(conn);
    DiscountAssignmentDAO assignmentDAO = new DiscountAssignmentDAOImpl(conn);
    DiscountService discountService = new DiscountServiceImpl(assignmentDAO, discountDAO);

    CategoryDAO categoryDAO = new CategoryDAOImpl(conn); // ✅ create once and reuse

    return new ItemServiceImpl(
        new ItemDAOImpl(conn),  // ✅ fixed constructor
        categoryDAO,
        new ItemMapper(),
        discountService
    );
}



    public static CategoryService createCategoryService(Connection conn) {
        return new CategoryServiceImpl(new CategoryDAOImpl(conn), new CategoryMapper());
    }

  public static CartService createCartService(Connection conn) {
    CartItemDAO cartItemDAO = new CartItemDAOimpl(conn);
    SessionCartManager sessionCartManager = new SessionCartManager();
    DiscountDAO discountDAO = new DicountDAOimpl(conn);
    DiscountAssignmentDAO assignmentDAO = new DiscountAssignmentDAOImpl(conn);
    DiscountService discountService = new DiscountServiceImpl(assignmentDAO, discountDAO);
    ItemMapper itemMapper = new ItemMapper();

    return new CartServiceImpl(cartItemDAO, sessionCartManager, discountService, itemMapper);
}


    public static CategoryCache createCategoryCache(CategoryService service, ServletContext ctx) {
        return new CategoryCache(service, ctx);
    }
}
