package util;

public class ErrorPageResolver {

    public static String resolve(String userType) {
        return switch (userType) {
            case "customer" -> "customerRegister.jsp";
            case "admin" -> "AdminRegister.jsp";
            case "staff" -> "staffRegister.jsp";
            default -> "index.jsp";
        };
    }
}
