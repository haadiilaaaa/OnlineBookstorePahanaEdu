package util;

import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;

public class RequestParamParser {

    public static BigDecimal parseBigDecimal(HttpServletRequest req, String paramName) {
        String value = req.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for parameter " + paramName + ": " + value);
            e.printStackTrace();
            return null;
        }
    }

    public static String getString(HttpServletRequest req, String paramName) {
        String value = req.getParameter(paramName);
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }
}
