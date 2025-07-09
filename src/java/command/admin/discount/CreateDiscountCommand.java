package command.admin.discount;

import dto.DiscountDTO;
import service.admin.DiscountManagementService;
import util.contannts.ParameterKeys;
import util.contannts.SessionKeys;
import util.contannts.SuccessMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateDiscountCommand implements DiscountActionCommand {

    private final DiscountManagementService discountService;

    public CreateDiscountCommand(DiscountManagementService discountService) {
        this.discountService = discountService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        DiscountDTO dto = new DiscountDTO();
        dto.setName(req.getParameter(ParameterKeys.NAME));
        dto.setDescription(req.getParameter(ParameterKeys.DESCRIPTION));
        dto.setDiscountPercent(Double.parseDouble(req.getParameter(ParameterKeys.PERCENT)));
        dto.setStartDate(java.sql.Date.valueOf(req.getParameter(ParameterKeys.START_DATE)));
        dto.setEndDate(java.sql.Date.valueOf(req.getParameter(ParameterKeys.END_DATE)));
        dto.setActive(Boolean.parseBoolean(req.getParameter(ParameterKeys.ACTIVE)));

        discountService.createDiscount(dto);
        req.getSession().setAttribute(SessionKeys.SUCCESS_MESSAGE, SuccessMessages.DISCOUNT_CREATED);
    }
}
