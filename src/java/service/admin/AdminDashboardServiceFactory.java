package service.admin;

import dao.*;
import db.DBConnection;
import util.IDGenerator;
import util.NextSequentialIDGenerator;

import java.sql.Connection;
import java.util.Collections;

public class AdminDashboardServiceFactory {

    public static AdminDashoardService createDashboardService(Connection conn) {
        CategoryDAO categoryDAO = new CategoryDAOImpl(conn);

        // Create a dummy IDGenerator for the DiscountDAO since the dashboard only reads data.
        // We can pass an empty list of existing IDs as this factory's purpose is not to save new data.
        IDGenerator<String> dummyDiscountIdGenerator = new NextSequentialIDGenerator("dis", Collections.emptyList());
        DiscountDAO discountDAO = new DicountDAOimpl(conn, dummyDiscountIdGenerator);

        return new AdminDashboardServiceImpl(
            new CustomerDAOimpl(conn),
            new StaffDAOImpl(conn),
            new ItemDAOImpl(conn),
            categoryDAO,
            discountDAO,
            new AminDAOImpl(conn)
        );
    }
}