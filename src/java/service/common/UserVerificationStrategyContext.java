// service.common.UserVerificationStrategyContext.java
package service.common;

import java.util.Map;

public class UserVerificationStrategyContext {

    private final Map<String, UserVerificationStrategy> strategyMap;

    public UserVerificationStrategyContext(Map<String, UserVerificationStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public void verify(String userType, String userId) throws Exception {
        UserVerificationStrategy strategy = strategyMap.get(userType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown user type: " + userType);
        }
        strategy.verify(userId);
    }
}
