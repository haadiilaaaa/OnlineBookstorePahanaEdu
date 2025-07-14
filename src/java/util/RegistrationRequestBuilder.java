package util;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistrationRequestBuilder {

    private static final Map<String, RequestToDTOConverter> converters = new ConcurrentHashMap<>();

    static {
        converters.put("customer", new CustomerRequestToDTOConverter());
        converters.put("admin", new AdminRequestToDTOConverter());
        converters.put("staff", new StaffRequestToDTOConverter());
    }

    public static Object buildDTO(String userType, HttpServletRequest request) {
        RequestToDTOConverter converter = converters.get(userType);
        if (converter == null) {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
        return converter.convert(request);
    }

    // Allows dynamic addition of converters to respect OCP
    public static void registerConverter(String userType, RequestToDTOConverter converter) {
        converters.put(userType, converter);
    }
}
