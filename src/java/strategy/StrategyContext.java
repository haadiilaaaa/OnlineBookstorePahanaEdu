package strategy;

import java.util.HashMap;
import java.util.Map;

public class StrategyContext {

    private final Map<String, RegistrationStrategy> strategyMap = new HashMap<>();

    public void addStrategy(String userType, RegistrationStrategy strategy) {
        strategyMap.put(userType, strategy);
    }

    public void executeStrategy(String userType, Object dto) throws Exception {
        RegistrationStrategy strategy = strategyMap.get(userType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for user type: " + userType);
        }
        strategy.register(dto);
    }
}
