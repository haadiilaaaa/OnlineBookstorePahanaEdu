package util;

import util.contannts.AttributeKeys;
import util.contannts.PagePaths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorNavigator {

    public static void forwardToError(String context, Exception e, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println(context + " Error: " + e.getMessage());
        e.printStackTrace(); // Prints full stack trace to console

        req.setAttribute(AttributeKeys.ERROR, "An error occurred: " + e.getMessage());
        req.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(req, resp);
    }
}
