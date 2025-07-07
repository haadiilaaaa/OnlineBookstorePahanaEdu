package controller.admin;

import db.DBConnection;
import dto.DiscountDTO;
import dto.DiscountAssignmentDTO;
import service.admin.DiscountManagementService;
import service.admin.DiscountManagementServiceImpl;
import dao.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ManageDiscountServlet extends HttpServlet {

    private DiscountManagementService discountService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();

            DiscountDAO discountDAO = new DicountDAOimpl(conn); // spelling: DicountDAO -> ok if intentional
            DiscountAssignmentDAO assignmentDAO = new DiscountAssignmentDAOImpl(conn);
            ItemDAO itemDAO = new ItemDAOImpl(conn);
            CategoryDAO categoryDAO = new CategoryDAOImpl(conn);

            this.discountService = new DiscountManagementServiceImpl(
                    discountDAO,
                    assignmentDAO,
                    itemDAO,
                    categoryDAO
            );
        } catch (Exception e) {
            throw new ServletException("Failed to init ManageDiscountServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<DiscountDTO> discounts = discountService.getAllDiscounts();
            Map<String, List<DiscountAssignmentDTO>> assignmentMap = discountService.getAssignmentMap();

            req.setAttribute("discounts", discounts);
            req.setAttribute("discountAssignmentsMap", assignmentMap);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load discounts.");
        }

        req.getRequestDispatcher("/admin/manageDiscounts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        try {
            if ("create".equals(action)) {
                DiscountDTO dto = new DiscountDTO();
                dto.setName(req.getParameter("name"));
                dto.setDescription(req.getParameter("description"));
                dto.setDiscountPercent(Double.parseDouble(req.getParameter("percent")));
                dto.setStartDate(java.sql.Date.valueOf(req.getParameter("start")));
                dto.setEndDate(java.sql.Date.valueOf(req.getParameter("end")));
                dto.setActive(Boolean.parseBoolean(req.getParameter("active")));

                discountService.createDiscount(dto);
                session.setAttribute("success", "Discount created!");
            }

            else if ("assign".equals(action)) {
                DiscountAssignmentDTO dto = new DiscountAssignmentDTO();
                dto.setDiscountId(req.getParameter("discountId"));
                dto.setType(req.getParameter("type"));
                dto.setItemId(req.getParameter("itemId"));
                dto.setCategoryId(req.getParameter("categoryId"));

                discountService.assignDiscount(dto);
                session.setAttribute("success", "Discount assigned!");
            }

            else if ("removeAssignment".equals(action)) {
                String assignmentId = req.getParameter("assignmentId");
                discountService.removeAssignment(assignmentId);
                session.setAttribute("success", "Assignment removed!");

                // 🔁 Redirect to the servlet (not JSP) to re-trigger doGet
                resp.sendRedirect("ManageDiscountServlet");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Operation failed: " + e.getMessage());
        }

        doGet(req, resp);
    }
}
