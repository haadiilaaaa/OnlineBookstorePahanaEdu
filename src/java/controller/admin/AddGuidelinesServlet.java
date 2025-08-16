package controller.admin;

import service.admin.AdminGuidelineService;
import service.admin.AdminGuidelineServiceProvider;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class AddGuidelinesServlet extends HttpServlet {

    private AdminGuidelineService guidelineService;

    @Override
    public void init() throws ServletException {
        try {
            this.guidelineService = AdminGuidelineServiceProvider.getService();
            System.out.println("AddGuidelinesServlet initialized. Service: " + guidelineService);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize AdminGuidelineService", e);
        }
    }

  @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String title = request.getParameter("title");
    String content = request.getParameter("content");
    HttpSession session = request.getSession();

    try {
        guidelineService.createGuideline(title, content);
        session.setAttribute("successMessage", "Guideline added successfully!");
    } catch (IllegalArgumentException e) {
        session.setAttribute("errorMessage", e.getMessage());
    } catch (Exception e) {
        session.setAttribute("errorMessage", "Failed to add guideline: " + e.getMessage());
    }

    response.sendRedirect("ManageGuidelinesServlet");
}


}
