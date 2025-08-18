package controller.admin;

import service.admin.AdminOrderService;
import util.EmailSender;
import util.contannts.PagePaths;
import util.contannts.AttributeKeys;
import util.contannts.SuccessMessages;
import util.contannts.ErrorMessages;

import javax.servlet.*;  
import javax.servlet.http.*;
import java.io.IOException;

public class AssignDeliveryPartnerServlet extends HttpServlet {

    private AdminOrderService adminOrderService;
    private EmailSender emailSender;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.adminOrderService = (AdminOrderService) context.getAttribute("AdminOrderService");
        this.emailSender = (EmailSender) context.getAttribute("EmailSender");

        if (adminOrderService == null || emailSender == null) {
            System.out.println("Dependencies not found in ServletContext.");
            throw new ServletException("Dependencies not found in ServletContext.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderId = request.getParameter("orderId");
        String partnerId = request.getParameter("partnerId");

        try {
            boolean updated = adminOrderService.assignDeliveryPartner(orderId, partnerId);

            if (updated) {
                // Get customer email
                String customerEmail = adminOrderService.getCustomerEmailByOrderId(orderId);
                String subject = "Delivery Partner Assigned for Your Order";
                String body = "Dear Customer,\n\nA delivery partner has been assigned to your Order ID: " + orderId +
                              ". You will be contacted soon for the delivery.\n\nThank you for shopping with us!\nOnline Bookstore Team";

                emailSender.sendEmail(customerEmail, subject, body);

                request.setAttribute(AttributeKeys.SUCCESS, SuccessMessages.PARTNER_ASSIGNED);
            } else {
                request.setAttribute(AttributeKeys.ERROR_MESSAGE, ErrorMessages.PARTNER_ASSIGN_FAILED);
            }

        } catch (Exception e) {
            System.out.println("Failed to assign delivery partner: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute(AttributeKeys.ERROR_MESSAGE, ErrorMessages.INTERNAL_SERVER_ERROR);
        }

        response.sendRedirect(request.getContextPath() + "/AdminOrderHistoryServlet");
    }
}
