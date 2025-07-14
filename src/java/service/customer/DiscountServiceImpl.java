package service.customer;

import dao.DiscountAssignmentDAO;
import dao.DiscountDAO;
import model.Discount;
import model.DiscountAssignment;
import model.DiscountMetaData;
import model.Item;
import service.customer.discount.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DiscountServiceImpl implements DiscountService {
    private final DiscountAssignmentDAO assignmentDAO;
    private final DiscountDAO discountDAO;

    // Map discount types to their strategies
    private final Map<String, DiscountStrategy> strategyMap = new HashMap<>();

    public DiscountServiceImpl(DiscountAssignmentDAO assignmentDAO, DiscountDAO discountDAO) {
        this.assignmentDAO = assignmentDAO;
        this.discountDAO = discountDAO;

        // Register discount strategies
       strategyMap.put("PERCENT", new PercentageDiscountStrategy());

        // Add more strategies here if needed
    }

    @Override
    public BigDecimal applyBestDiscount(Item item) throws Exception {
        if (item == null) throw new IllegalArgumentException("Item cannot be null");

        BigDecimal originalPrice = item.getPrice();
        BigDecimal discountedPrice = originalPrice;

        Discount bestDiscount = null;

        List<DiscountAssignment> assignments = assignmentDAO.findAssignmentsForItem(
            item.getId(),
            item.getCategoryId()
        );

        Date now = new Date();

        for (DiscountAssignment assignment : assignments) {
            Discount discount = discountDAO.findById(assignment.getDiscountId());
            if (discount != null && discount.isActive()
                    && !now.before(discount.getStartDate())
                    && !now.after(discount.getEndDate())) {

                if (bestDiscount == null ||
                    discount.getDiscountPercent() > bestDiscount.getDiscountPercent()) {
                    bestDiscount = discount;
                }
            }
        }

        DiscountMetaData meta = new DiscountMetaData();

       if (bestDiscount != null) {
    // Instead of reading discount type from DB (which doesn't exist), hardcode "PERCENT"
    String discountType = "PERCENT";

    DiscountStrategy strategy = strategyMap.get(discountType);
    if (strategy == null) {
        throw new IllegalStateException("No strategy found for discount type: " + discountType);
    }

    discountedPrice = strategy.calculateDiscountedPrice(item, bestDiscount);

    BigDecimal discountAmount = originalPrice.subtract(discountedPrice);

    meta.setHasDiscount(true);
    meta.setDiscountedPrice(discountedPrice);
    meta.setDiscountLabel(bestDiscount.getName());
    meta.setDiscountAmount(discountAmount);
    meta.setDiscountType(discountType);
} else {
    meta.setHasDiscount(false);
    meta.setDiscountedPrice(originalPrice);
    meta.setDiscountAmount(BigDecimal.ZERO);
    meta.setDiscountType(null);
    meta.setDiscountLabel(null);
}


        item.setDiscount(meta);
        return discountedPrice;
    }
}
