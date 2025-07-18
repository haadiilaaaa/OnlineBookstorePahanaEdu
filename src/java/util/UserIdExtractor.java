package util;

import dto.CustomerDTO;
import dto.AdminDTO;
import dto.StaffDTO;
import dto.DeliveryPartnerDTO; // ✅ Import this

public class UserIdExtractor {

    public static String extractId(String userType, Object dto) {
         System.out.println("Extracting ID. userType=" + userType + ", dto class=" + dto.getClass().getName());
        return switch (userType) {
            case "customer" -> ((CustomerDTO) dto).getId();
            case "admin"    -> ((AdminDTO) dto).getId();
            case "staff"    -> ((StaffDTO) dto).getId();
            case "delivery" -> ((DeliveryPartnerDTO) dto).getId(); // ✅ Fix here
            default -> throw new IllegalArgumentException("Unknown type: " + userType);
        };
    }
}
