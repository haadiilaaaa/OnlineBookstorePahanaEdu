// controller/admin/ManageDiscountServlet.java
package controller.admin;

import command.admin.discount.DiscountActionCommand;
import command.admin.discount.DiscountCommandFactory;
import dto.DiscountDTO;
import dto.DiscountAssignmentDTO;
import service.admin.DiscountManagementService;
import util.contannts.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;   
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageDiscountServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ManageDiscountServlet.class.getName());

    private DiscountManagementService discountService;
    private DiscountCommandFactory commandFactory;

    @Override
    public void init() throws ServletException {
        discountService = (DiscountManagementService)
                getServletContext().getAttribute(ContextKeys.DISCOUNT_MANAGEMENT_SERVICE);

        if (discountService == null) {
            throw new ServletException("DiscountManagementService not found in ServletContext.");
        }

        commandFactory = new DiscountCommandFactory(discountService);
        LOGGER.info("[INIT] DiscountCommandFactory initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<DiscountDTO> discounts = discountService.getAllDiscounts();
            Map<String, List<DiscountAssignmentDTO>> assignmentMap = discountService.getAssignmentMap();

            req.setAttribute(AttributeKeys.DISCOUNTS, discounts);
            req.setAttribute(AttributeKeys.DISCOUNT_ASSIGNMENTS_MAP, assignmentMap);
            req.getRequestDispatcher(PagePaths.MANAGE_DISCOUNTS_PAGE).forward(req, resp);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[GET] Failed to load discounts", e);
            req.setAttribute(AttributeKeys.ERROR_MESSAGE, ErrorMessages.FAILED_TO_LOAD_DISCOUNTS);
            req.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ParameterKeys.ACTION);

        try {
            if (action == null || action.trim().isEmpty()) {
                throw new IllegalArgumentException("Action parameter is required.");
            }

            DiscountActionCommand command = commandFactory.getCommand(action);
            command.execute(req, resp);

            if (ParameterKeys.ACTION_REMOVE_ASSIGNMENT.equals(action)) {
                resp.sendRedirect(PagePaths.MANAGE_DISCOUNTS_SERVLET);
            } else {
                doGet(req, resp);
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "[POST] Discount operation failed", e);
            req.setAttribute(AttributeKeys.ERROR_MESSAGE, ErrorMessages.OPERATION_FAILED);
            doGet(req, resp);
        }
    }
}
