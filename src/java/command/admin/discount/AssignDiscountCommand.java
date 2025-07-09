package command.admin.discount;

import dto.DiscountAssignmentDTO;
import service.admin.DiscountManagementService;
import util.contannts.ParameterKeys;
import util.contannts.SessionKeys;
import util.contannts.SuccessMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AssignDiscountCommand implements DiscountActionCommand {

    private final DiscountManagementService discountService;

    public AssignDiscountCommand(DiscountManagementService discountService) {
        this.discountService = discountService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        DiscountAssignmentDTO dto = new DiscountAssignmentDTO();
        dto.setDiscountId(req.getParameter(ParameterKeys.DISCOUNT_ID));
        dto.setType(req.getParameter(ParameterKeys.TYPE));
        dto.setItemId(req.getParameter(ParameterKeys.ITEM_ID));
        dto.setCategoryId(req.getParameter(ParameterKeys.CATEGORY_ID));

        discountService.assignDiscount(dto);
        req.getSession().setAttribute(SessionKeys.SUCCESS_MESSAGE, SuccessMessages.DISCOUNT_ASSIGNED);
    }
}
