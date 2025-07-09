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
        return new ItemServiceImpl(
            new ItemDAOImpl(conn),
            new CategoryDAOImpl(conn),
            new ItemMapper(),
            new DicountDAOimpl(conn),
            new DiscountAssignmentDAOImpl(conn)
        );
    }

    public static CategoryService createCategoryService(Connection conn) {
        return new CategoryServiceImpl(new CategoryDAOImpl(conn), new CategoryMapper());
    }

    public static CartService createCartService(Connection conn) {
        return new CartServiceImpl(new CartItemDAOimpl(conn));
    }

    public static CategoryCache createCategoryCache(CategoryService service, ServletContext ctx) {
        return new CategoryCache(service, ctx);
    }
}
