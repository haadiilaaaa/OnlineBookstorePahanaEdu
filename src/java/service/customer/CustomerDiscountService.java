package service.customer;

import dao.DiscountAssignmentDAO;
import dao.DiscountDAO;
import model.Discount;
import model.DiscountAssignment;
import model.Item;

import java.math.BigDecimal;
import java.util.List;

public class CustomerDiscountService {
    private final DiscountAssignmentDAO assignmentDAO;
    private final DiscountDAO discountDAO;

    public CustomerDiscountService(DiscountAssignmentDAO assignmentDAO, DiscountDAO discountDAO) {
        this.assignmentDAO = assignmentDAO;
        this.discountDAO = discountDAO;
    }

    /**
     * Calculate the best applicable discount for the given item and category.
     * Returns discounted price based on all applicable discounts.
     */
    // Method must exist exactly like this
    public BigDecimal calculateDiscountedPrice(Item item) throws Exception {
        BigDecimal originalPrice = item.getPrice();
        BigDecimal discountedPrice = originalPrice;

        List<DiscountAssignment> assignments = assignmentDAO.findAssignmentsForItem(item.getId(), item.getCategoryId());

        for (DiscountAssignment assignment : assignments) {
            Discount discount = discountDAO.findById(assignment.getDiscountId());

            if (discount != null && discount.isActive() && discount.isWithinDateRange()) {
                BigDecimal discountPercent = BigDecimal.valueOf(discount.getDiscountPercent()).divide(BigDecimal.valueOf(100));
                BigDecimal priceAfterDiscount = originalPrice.multiply(BigDecimal.ONE.subtract(discountPercent));

                if (priceAfterDiscount.compareTo(discountedPrice) < 0) {
                    discountedPrice = priceAfterDiscount;
                }
            }
        }

        return discountedPrice;
    }
}

