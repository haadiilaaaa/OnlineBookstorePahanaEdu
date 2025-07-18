package controller.admin;

import dao.DeliveryPartnerDAO;
import dao.DeliveryPartnerDAOImpl;
import db.DBConnection;
import dto.DeliveryPartnerDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;


public class ManageDeliveryPartnersServlet extends HttpServlet {

    private DeliveryPartnerDAO deliveryPartnerDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            deliveryPartnerDAO = new DeliveryPartnerDAOImpl(conn);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize DAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<DeliveryPartnerDTO> pendingPartners = deliveryPartnerDAO.findByStatus("pending");

            request.setAttribute("pendingPartners", pendingPartners);
            request.getRequestDispatcher("/admin/manageDeliveryPartners.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Failed to fetch pending delivery partners", e);
        }
    }
}
