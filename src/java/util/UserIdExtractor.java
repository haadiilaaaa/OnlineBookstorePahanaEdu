package util;

import dto.CustomerDTO;
import dto.AdminDTO;
import dto.StaffDTO;
import dto.DeliveryPartnerDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class UserIdExtractor {

    private static final Map<String, BiFunction<String, Object, String>> extractors = new ConcurrentHashMap<>();

    static {
        extractors.put("customer", (userType, dto) -> ((CustomerDTO) dto).getId());
        extractors.put("admin",    (userType, dto) -> ((AdminDTO) dto).getId());
        extractors.put("staff",    (userType, dto) -> ((StaffDTO) dto).getId());
        extractors.put("delivery", (userType, dto) -> ((DeliveryPartnerDTO) dto).getId());
    }

    public static String extractId(String userType, Object dto) {
        BiFunction<String, Object, String> extractor = extractors.get(userType);
        if (extractor == null) {
            throw new IllegalArgumentException("Unknown type: " + userType);
        }
        return extractor.apply(userType, dto);
    }

    // ✅ This lets you inject custom logic in tests
    public static void registerExtractor(String userType, BiFunction<String, Object, String> extractor) {
        extractors.put(userType, extractor);
    }
}
