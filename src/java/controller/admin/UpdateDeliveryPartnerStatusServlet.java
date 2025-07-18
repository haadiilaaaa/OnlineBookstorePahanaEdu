package controller.admin;

import dao.DeliveryPartnerDAO;
import db.DBConnection;
import util.DAOExeption;
import dao.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import util.EmailSender;
import util.EmailServiceFactory;
import model.DeliveryPartner;
import java.util.Optional;



public class UpdateDeliveryPartnerStatusServlet extends HttpServlet {

    private DeliveryPartnerDAO deliveryPartnerDAO;

   @Override
public void init() throws ServletException {
    try {
        Connection conn = DBConnection.getInstance().getConnection();
        deliveryPartnerDAO = new DeliveryPartnerDAOImpl(conn);
    } catch (SQLException e) {
        throw new ServletException("Failed to initialize DeliveryPartnerDAO", e);
    }
}

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String partnerId = request.getParameter("partnerId");
    String action = request.getParameter("action");

    if (partnerId == null || action == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing partnerId or action");
        return;
    }

    try {
        // ✅ Determine status based on action
        String newStatus;
        String subject;
        String message;

        Optional<DeliveryPartner> partnerOpt = deliveryPartnerDAO.findById(partnerId);
        if (partnerOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Partner not found");
            return;
        }

        DeliveryPartner partner = partnerOpt.get();

        if ("approve".equals(action)) {
            newStatus = "approved";

            subject = "Approval Confirmed";
            message = "Hi " + partner.getFirstName() + ",\n\n"
                    + "Your account as a delivery partner has been approved.\n"
                    + "You may now log in using your credentials.\n\n"
                    + "Best regards,\nAdmin Team";

        } else if ("reject".equals(action)) {
            newStatus = "rejected";

            subject = "Application Rejected";
            message = "Hi " + partner.getFirstName() + ",\n\n"
                    + "We're sorry to inform you that your application as a delivery partner was rejected.\n"
                    + "If you believe this was a mistake, please contact support.\n\n"
                    + "Best regards,\nAdmin Team";

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            return;
        }

        // ✅ Update status in the database
        deliveryPartnerDAO.updateStatus(partnerId, newStatus);

        // ✅ Send email notification
        EmailSender emailSender = EmailServiceFactory.createGeneralEmailService();
        emailSender.sendEmail(partner.getEmail(), subject, message);

        // ✅ Redirect back to dashboard
        response.sendRedirect("ManageDeliveryPartnersServlet");

    } catch (Exception e) {
        throw new ServletException("Error updating delivery partner status or sending email", e);
    }
}

}
