package command.admin.discount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DiscountActionCommand {
    void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
