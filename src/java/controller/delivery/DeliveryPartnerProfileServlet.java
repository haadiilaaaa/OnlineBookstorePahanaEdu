package controller.delivery;

import dto.DeliveryPartnerDTO;
import service.deliveryPartner.DeliveryPartnerProfileService;
import service.deliveryPartner.DeliveryPartnerProfileServiceImpl;
import dao.DeliveryPartnerDAOImpl;
import dao.OrderDAOImpl;
import db.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

public class DeliveryPartnerProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String partnerId = ((dto.UserSession) session.getAttribute("user")).getId();

        try (var conn = DBConnection.getInstance().getConnection()) {
            DeliveryPartnerDAOImpl dpDAO = new DeliveryPartnerDAOImpl(conn);
            OrderDAOImpl orderDAO = new OrderDAOImpl(conn, null); // pass orderItemDAO if needed
            DeliveryPartnerProfileService profileService = new DeliveryPartnerProfileServiceImpl(dpDAO, orderDAO);

            DeliveryPartnerDTO profile = profileService.getProfile(partnerId);
            BigDecimal earnings = profileService.getTotalEarnings(partnerId);

            // ✅ Check if a success message exists in session (flash message)
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                request.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage"); // show once only
            }

            request.setAttribute("profile", profile);
            request.setAttribute("earnings", earnings);

            request.getRequestDispatcher("/deliveryPartner/profile.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load profile.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle form submission for profile update
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String partnerId = ((dto.UserSession) session.getAttribute("user")).getId();

        try (var conn = DBConnection.getInstance().getConnection()) {
            DeliveryPartnerDAOImpl dpDAO = new DeliveryPartnerDAOImpl(conn);
            OrderDAOImpl orderDAO = new OrderDAOImpl(conn, null);
            DeliveryPartnerProfileService profileService = new DeliveryPartnerProfileServiceImpl(dpDAO, orderDAO);

            DeliveryPartnerDTO dto = new DeliveryPartnerDTO();
            dto.setId(partnerId);
            dto.setFirstName(request.getParameter("firstName"));
            dto.setLastName(request.getParameter("lastName"));
            dto.setEmail(request.getParameter("email"));
            dto.setContactNumber(request.getParameter("contactNumber"));
            dto.setVehicleNumber(request.getParameter("vehicleNumber"));

            profileService.updateProfile(dto);
            // in doPost after setAttribute
System.out.println("✅ SuccessMessage set: " + session.getAttribute("successMessage"));

// in doGet before moving to request
System.out.println("➡ SuccessMessage from session: " + session.getAttribute("successMessage"));

            // ✅ Add success message to session so it survives redirect
            session.setAttribute("successMessage", "Profile updated successfully!");

            // Redirect to GET (PRG pattern)
            response.sendRedirect("DeliveryPartnerProfileServlet");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update profile");
            request.getRequestDispatcher("/deliveryPartner/profile.jsp").forward(request, response);
        }
    }
}
