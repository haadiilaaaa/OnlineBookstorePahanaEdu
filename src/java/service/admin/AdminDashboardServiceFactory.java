package service.admin;

import dao.*;
import db.DBConnection;
import service.admin.AdminDashoardService;
import service.admin.AdminDashboardServiceImpl;


import java.sql.Connection;

public class AdminDashboardServiceFactory {

    public static AdminDashoardService createDashboardService(Connection conn) {
        return new AdminDashboardServiceImpl(
            new CustomerDAOimpl(conn),
            new StaffDAOImpl(conn),
            new ItemDAOImpl(conn),
            new CategoryDAOImpl(conn),
            new DicountDAOimpl(conn),
            new AminDAOImpl(conn)
        );
    }
}
