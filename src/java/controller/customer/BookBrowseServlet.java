package controller.customer;

import dto.UserSession;
import handler.customer.BookBrowseHandler;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

import static util.contannts.AttributeKeys.ERROR;
import static util.contannts.PagePaths.ERROR_PAGE;
import static util.contannts.SessionKeys.ERROR_MESSAGE;
import static util.contannts.SessionKeys.SUCCESS_MESSAGE;

public class BookBrowseServlet extends BaseCustomerServlet {

    private BookBrowseHandler browseHandler;

    // Setter for injection.
    public void setBrowseHandler(BookBrowseHandler handler) {
        this.browseHandler = handler;
    }

    @Override
    public void init() throws ServletException {
        // The servlet now gets its handler from the ServletContext,
        // assuming a listener or factory has already created and configured it.
        this.browseHandler = (BookBrowseHandler) getServletContext().getAttribute("BookBrowseHandler");

        if (this.browseHandler == null) {
            throw new ServletException("BookBrowseHandler not initialized in ServletContext.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UserSession user = getAuthenticatedUser(req, resp);
            if (user == null) {
                return; // User is not authenticated, getAuthenticatedUser has already redirected.
            }

            // --- THIS IS THE CRITICAL ADDITION ---
            // Fetch messages from the session and transfer them to the request scope.
            // This ensures they are available to the JSP for display.
            // The `AddToCartServlet` redirects here after adding an item,
            // so the messages are stored in the session.
            HttpSession session = req.getSession();
            String successMsg = (String) session.getAttribute(SUCCESS_MESSAGE);
            String errorMsg = (String) session.getAttribute(ERROR_MESSAGE);

            if (successMsg != null) {
                req.setAttribute(SUCCESS_MESSAGE, successMsg);
                session.removeAttribute(SUCCESS_MESSAGE); // Clear the message from session after reading
            }

            if (errorMsg != null) {
                req.setAttribute(ERROR_MESSAGE, errorMsg);
                session.removeAttribute(ERROR_MESSAGE); // Clear the message from session after reading
            }
            // -------------------------------------

            // All business logic is now handled by the browseHandler.
            browseHandler.handleRequest(req, resp);

        } catch (Exception e) {
            System.out.println("Failed to process book browse request: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute(ERROR, "Failed to load books.");
            req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
        }
    }
}