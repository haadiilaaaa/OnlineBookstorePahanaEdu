package util.redirect;

import java.util.Map;

public class RedirectStrategyRegistry {
    private static final Map<String, LoginRedirectStrategy> strategyMap = Map.of(
        "customer", new CustomerRedirectStrategy(),
        "admin", new AdminRedirectStrategy(),
        "staff", new StaffRedirectStrategy()
    );

    public static LoginRedirectStrategy getStrategy(String role) {
        if (role == null) return null;
        return strategyMap.get(role.toLowerCase());
    }
}
