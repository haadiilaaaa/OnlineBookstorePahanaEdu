// controller/ResetPasswordServlet.java
package controller;

import service.common.ResetPasswordService;
import util.*;
import util.contannts.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class ResetPasswordServlet extends HttpServlet {

    private ResetPasswordService resetPasswordService;

    @Override
    public void init() throws ServletException {
        try {
            resetPasswordService = (ResetPasswordService) getServletContext().getAttribute("ResetPasswordService");
        } catch (Exception e) {
            throw new ServletException("ResetPasswordService not found", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String token = req.getParameter(ParameterKeys.TOKEN);
        String password = req.getParameter(ParameterKeys.NEW_PASSWORD);
        String confirmPassword = req.getParameter(ParameterKeys.CONFIRM_PASSWORD);

        try {
            resetPasswordService.resetPassword(token, password, confirmPassword);
            HttpSession session = req.getSession();
            session.setAttribute(SessionKeys.SUCCESS_MESSAGE, MessageResolver.get("reset.success"));
           resp.sendRedirect(req.getContextPath() + PagePaths.LOGIN_PAGE);

        } catch (IllegalArgumentException | IllegalStateException e) {
            req.setAttribute(ParameterKeys.TOKEN, token);
            forwardWithError(req, resp, e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute(ParameterKeys.TOKEN, token);
            forwardWithError(req, resp, MessageResolver.get("reset.internal_error"));
        }  
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp, String errorMsg)
            throws ServletException, IOException {
        req.setAttribute(AttributeKeys.ERROR, errorMsg);
      req.getRequestDispatcher(PagePaths.RESET_PASSWORD_PAGE).forward(req, resp);
    }
}
