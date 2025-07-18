package service.admin;

import dao.OrderDAOImpl;
import db.DBConnection;
import service.admin.AdminOrderService;
import service.admin.AdminOrderServiceImpl;
import dao.*;

import java.sql.Connection;

public class AdminOrderServicefactory {

    public static AdminOrderService createAdminOrderService(Connection conn) {
        OrderItemDAO orderItemDAO = new OrderItemDAOImpl(conn);
        OrderDAO orderDAO = new OrderDAOImpl(conn, orderItemDAO);
        DeliveryPartnerDAO deliveryPartnerDAO = new DeliveryPartnerDAOImpl(conn);
        
        return new AdminOrderServiceImpl(orderDAO, deliveryPartnerDAO);
    }
}

