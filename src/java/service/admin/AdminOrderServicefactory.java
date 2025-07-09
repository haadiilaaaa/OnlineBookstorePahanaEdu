package service.admin;

import dao.OrderDAOImpl;
import db.DBConnection;
import service.admin.AdminOrderService;
import service.admin.AdminOrderServiceImpl;

import java.sql.Connection;

public class AdminOrderServicefactory {

    public static AdminOrderService createAdminOrderService(Connection conn) {
        return new AdminOrderServiceImpl(new OrderDAOImpl(conn));
    }
}
