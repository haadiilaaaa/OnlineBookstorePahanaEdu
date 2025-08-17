package controller.customer;

import dto.CustomerDTO;
import model.Customer;
import service.customer.CustomerEditProfileService;
import util.ServiceException;
import util.ValidationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;  

@WebServlet("/customer/edit-profile")
public class EditCustomerProfileServlet extends HttpServlet {

    private final CustomerEditProfileService editProfileService = new CustomerEditProfileService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        dto.UserSession sessionUser = (dto.UserSession) session.getAttribute("user");


        CustomerDTO dto = new CustomerDTO();
        dto.setId(sessionUser.getId());
        dto.setUsername(sessionUser.getUsername());
        dto.setFirstName(req.getParameter("firstName"));
        dto.setLastName(req.getParameter("lastName"));
        dto.setEmail(req.getParameter("email"));
        dto.setContactNumber(req.getParameter("contactNumber"));
        dto.setAddress(req.getParameter("address"));

        try {
            editProfileService.updateProfile(dto);
            req.setAttribute("message", "Profile updated successfully.");
            session.setAttribute("user", dto); // optionally update session
        } catch (ValidationException ve) {
            req.setAttribute("error", ve.getMessage());
        } catch (ServiceException se) {
            req.setAttribute("error", "Something went wrong. Please try again.");
        }

        req.getRequestDispatcher("/customer/customerEditProfile.jsp").forward(req, resp);
    }
}
