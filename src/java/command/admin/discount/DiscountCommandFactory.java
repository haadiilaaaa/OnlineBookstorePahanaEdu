package command.admin.discount;

import service.admin.DiscountManagementService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class DiscountCommandFactory {

    private final Map<String, DiscountActionCommand> commandMap = new HashMap<>();

    public DiscountCommandFactory(DiscountManagementService discountService) {
        commandMap.put("create", new CreateDiscountCommand(discountService));
        commandMap.put("assign", new AssignDiscountCommand(discountService));
        commandMap.put("removeAssignment", new RemoveAssignmentCommand(discountService));
    }

    public DiscountActionCommand getCommand(String action) {
        return commandMap.getOrDefault(action, new DiscountActionCommand() {
            @Override
            public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
                throw new IllegalArgumentException("Unsupported action: " + action);
            }
        });
    }
}
