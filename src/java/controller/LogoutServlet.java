package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class LogoutServlet extends HttpServlet {

    /**
     * Handles the HTTP GET method.
     * The access modifier has been changed to `public` to allow direct testing.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current session, but don't create a new one if it doesn't exist.
        // This is important for the test case where no session is present.
        HttpSession session = request.getSession(false);
         
        // Check if a session exists before trying to invalidate it.
        if (session != null) {
            // Invalidate the session, which removes all its attributes.
            session.invalidate();
        }

        // Redirect the user to the login page after logging out.
        // This is the line that your test case is expecting and failing on.
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
