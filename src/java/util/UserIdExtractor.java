package util;
import dto.CustomerDTO;
import dto.AdminDTO;
import dto.StaffDTO;


public class UserIdExtractor {

    public static String extractId(String userType, Object dto) {
        return switch (userType) {
            case "customer" -> ((CustomerDTO) dto).getId();
            case "admin" -> ((AdminDTO) dto).getId();
            case "staff" -> ((StaffDTO) dto).getId();
            default -> throw new IllegalArgumentException("Unknown type");
        };
    }
}
