package command.admin.discount;

import service.admin.DiscountManagementService;
import util.contannts.ParameterKeys;
import util.contannts.SessionKeys;
import util.contannts.SuccessMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveAssignmentCommand implements DiscountActionCommand {

    private final DiscountManagementService discountService;

    public RemoveAssignmentCommand(DiscountManagementService discountService) {
        this.discountService = discountService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String assignmentId = req.getParameter(ParameterKeys.ASSIGNMENT_ID);
        discountService.removeAssignment(assignmentId);
        req.getSession().setAttribute(SessionKeys.SUCCESS_MESSAGE, SuccessMessages.ASSIGNMENT_REMOVED);
    }
}
