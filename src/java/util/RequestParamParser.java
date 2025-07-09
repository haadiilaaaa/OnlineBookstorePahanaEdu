package util;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class RequestParamParser {

    private static final Logger logger = Logger.getLogger(RequestParamParser.class.getName());

    public static BigDecimal parseBigDecimal(HttpServletRequest req, String paramName) {
        String value = req.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid number format for parameter " + paramName + ": " + value, e);
            return null;
        }
    }

    public static String getString(HttpServletRequest req, String paramName) {
        String value = req.getParameter(paramName);
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }
}
