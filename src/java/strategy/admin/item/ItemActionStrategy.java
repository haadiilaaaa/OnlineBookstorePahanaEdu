package strategy.admin.item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ItemActionStrategy {
    StrategyResult execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
