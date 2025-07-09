package strategy.admin.item;

import util.enums.ItemAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ItemStrategyExcecutor {
    private final Map<String, ItemActionStrategy> strategyMap;

    public ItemStrategyExcecutor(Map<String, ItemActionStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public StrategyResult execute(ItemAction action, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ItemActionStrategy strategy = strategyMap.getOrDefault(
            action != null ? action.value() : ItemAction.DEFAULT.value(),
            strategyMap.get(ItemAction.DEFAULT.value())
        );
        return strategy.execute(req, resp);
    }
}