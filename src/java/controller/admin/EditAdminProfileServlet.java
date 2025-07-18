package controller.admin;

import dto.AdminDTO;
import dto.UserSession;
import service.admin.AdminProfileService;
import service.admin.AdminProfileServiceImpl;
import dao.AminDAOImpl;
import db.DBConnection;
import util.DAOExeption;
import util.*;
import service.common.*;
import dao.*;
import java.util.List;
import model.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import dao.*;
import java.sql.Connection;

public class EditAdminProfileServlet extends HttpServlet {

    private AdminProfileService profileService;

   @Override
public void init() throws ServletException {
    try {
        Connection conn = DBConnection.getInstance().getConnection();

        AdminDAO adminDAO = new AminDAOImpl(conn); // ✅ FIXED: proper class name
        List<GenericUserDAO<? extends User>> daos = List.of(
            new AminDAOImpl(conn),
            new CustomerDAOimpl(conn),
            new StaffDAOImpl(conn)
        );

        profileService = new AdminProfileServiceImpl(adminDAO, daos); // ✅ pass both arguments
    } catch (Exception e) {
        throw new ServletException("Service initialization failed", e);
    }
}


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserSession user = (UserSession) session.getAttribute("user");

        try {
            AdminDTO adminDTO = profileService.getAdminProfile(user.getId());
            req.setAttribute("admin", adminDTO);
            req.getRequestDispatcher("/admin/editProfile.jsp").forward(req, resp);
        } catch (DAOExeption e) {
            req.setAttribute("error", "Failed to load profile: " + e.getMessage());
            req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
        }
    }

  @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    AdminDTO dto = new AdminDTO();
    dto.setId(req.getParameter("id"));
    dto.setUsername(req.getParameter("username"));
    dto.setFirstName(req.getParameter("firstName"));
    dto.setLastName(req.getParameter("lastName"));
    dto.setEmail(req.getParameter("email"));
    dto.setContactNumber(req.getParameter("contactNumber"));
    dto.setPassword(req.getParameter("password"));
    dto.setConfirmPassword(req.getParameter("confirmPassword"));

    try {
    // 1. Validate fields (without forcing password)
    AdminValidator adminValidator = new AdminValidator();
    adminValidator.validateForProfileUpdate(dto);

    // 2. Validate username/email uniqueness except current user
    List<GenericUserDAO<? extends User>> daos = List.of(
        new AminDAOImpl(DBConnection.getInstance().getConnection())
        // Add others: new CustomerDAOImpl(...), etc.
    );
    GlobalUserValidator globalValidator = new GlobalUserValidator(daos);
    globalValidator.validateUniqueUsernameAndEmail(dto.getUsername(), dto.getEmail(), dto.getId());

    // 3. Update profile
    profileService.updateAdminProfile(dto);

    req.setAttribute("success", "Profile updated successfully.");
    doGet(req, resp);

} catch (ValidationException e) {
    req.setAttribute("error", e.getMessage());
    doGet(req, resp);
} catch (Exception e) {
    req.setAttribute("error", "Unexpected error: " + e.getMessage());
    doGet(req, resp);
}

}


}
