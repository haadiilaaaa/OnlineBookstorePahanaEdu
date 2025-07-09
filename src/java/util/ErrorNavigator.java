package util;

import util.contannts.AttributeKeys;
import util.contannts.PagePaths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorNavigator {
    private static final Logger LOGGER = Logger.getLogger(ErrorNavigator.class.getName());

    public static void forwardToError(String context, Exception e, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOGGER.log(Level.WARNING, context + " Error: ", e);
        req.setAttribute(AttributeKeys.ERROR, "An error occurred: " + e.getMessage());
        req.getRequestDispatcher(PagePaths.ERROR_PAGE).forward(req, resp);
    }
}