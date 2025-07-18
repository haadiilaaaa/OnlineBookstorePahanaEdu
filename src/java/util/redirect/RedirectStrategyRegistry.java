package util.redirect;

import java.util.HashMap;
import java.util.Map;


public class RedirectStrategyRegistry {
    private static final Map<String, LoginRedirectStrategy> strategyMap = new HashMap<>();

    static {
        strategyMap.put("customer", new CustomerRedirectStrategy());
        strategyMap.put("admin", new AdminRedirectStrategy());
        strategyMap.put("staff", new StaffRedirectStrategy());
        strategyMap.put("delivery", new DeliveryRedirectStrategy());  // Add this line
    }

    public static LoginRedirectStrategy getStrategy(String role) {
        if (role == null) return null;
        return strategyMap.get(role.toLowerCase());
    }
}
