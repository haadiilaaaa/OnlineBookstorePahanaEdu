package service.customer;

import dao.*;
import mapper.*;
import model.CartItem; // Add this import
import service.admin.*;
import service.common.*;
import util.IDGenerator;
import util.NextSequentialIDGenerator; 
import javax.servlet.ServletContext;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemServiceFactory {

    public static ItemService createItemService(Connection conn) throws Exception {
        IDGenerator<String> discountIdGenerator = new NextSequentialIDGenerator("dis", Collections.emptyList());
        DiscountDAO discountDAO = new DicountDAOimpl(conn, discountIdGenerator);
        DiscountAssignmentDAO assignmentDAO = new DiscountAssignmentDAOImpl(conn);
        DiscountService discountService = new DiscountServiceImpl(assignmentDAO, discountDAO);
        CategoryDAO categoryDAO = new CategoryDAOImpl(conn);

        return new ItemServiceImpl(
            new ItemDAOImpl(conn),
            categoryDAO,
            new ItemMapper(),
            discountService
        );
    }

    public static CategoryService createCategoryService(Connection conn) {
        return new CategoryServiceImpl(new CategoryDAOImpl(conn), new CategoryMapper());
    }

    public static CartFacade createCartFacade(Connection conn) throws Exception {
        CartItemDAO cartItemDAO = new CartItemDAOimpl(conn);
        SessionCartManager sessionCartManager = new SessionCartManager();
        
        IDGenerator<String> discountIdGenerator = new NextSequentialIDGenerator("dis", Collections.emptyList());
        DiscountDAO discountDAO = new DicountDAOimpl(conn, discountIdGenerator);
        
        DiscountAssignmentDAO assignmentDAO = new DiscountAssignmentDAOImpl(conn);
        DiscountService discountService = new DiscountServiceImpl(assignmentDAO, discountDAO);
        ItemMapper itemMapper = new ItemMapper();

        // Correctly initialize CartServiceImpl with an IDGenerator
        List<String> existingCartIds = cartItemDAO.findAll().stream() // Use the new method
            .map(CartItem::getId)
            .collect(Collectors.toList());
        IDGenerator<String> cartIdGenerator = new NextSequentialIDGenerator("cart", existingCartIds);
        
        PersistentCartService persistentCartService = new CartServiceImpl(cartItemDAO, sessionCartManager, cartIdGenerator);
        SessionCartService sessionCartService = (SessionCartService) persistentCartService;

        return new CartFacade(persistentCartService, sessionCartService, discountService, itemMapper);
    }

    public static CategoryCache createCategoryCache(CategoryService service, ServletContext ctx) {
        return new CategoryCache(service, ctx);
    }
}