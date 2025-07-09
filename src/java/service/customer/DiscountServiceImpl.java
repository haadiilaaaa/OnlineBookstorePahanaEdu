package service.customer;

import dao.DiscountAssignmentDAO;
import dao.DiscountDAO;
import model.Item;
import dao.*;
import db.DBConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import model.*;
import java.util.List;


public class DiscountServiceImpl implements DiscountService {

    private DiscountAssignmentDAO assignmentDAO;
    private DiscountDAO discountDAO;

    public DiscountServiceImpl() {
        // Optional: initialize with default connection
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            this.assignmentDAO = new DiscountAssignmentDAOImpl(conn);
            this.discountDAO = new DicountDAOimpl(conn);
        } catch (Exception e) {
            throw new RuntimeException("DiscountServiceImpl init failed", e);
        }
    }

    // Or add this too for manual injection:
    public DiscountServiceImpl(DiscountAssignmentDAO assignmentDAO, DiscountDAO discountDAO) {
        this.assignmentDAO = assignmentDAO;
        this.discountDAO = discountDAO;
    }

   @Override
public BigDecimal applyBestDiscount(Item item) throws Exception {
    BigDecimal originalPrice = item.getPrice();
    BigDecimal discountedPrice = originalPrice;

    Discount bestDiscount = null;

    // Load all matching discount assignments
    List<DiscountAssignment> assignments = assignmentDAO.findAssignmentsForItem(
        item.getId(),
        item.getCategoryId()
    );

    java.util.Date now = new java.util.Date();

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

    // If best discount found, apply it
    if (bestDiscount != null) {
        BigDecimal discountAmount = originalPrice
                .multiply(BigDecimal.valueOf(bestDiscount.getDiscountPercent()))
                .divide(BigDecimal.valueOf(100));

        discountedPrice = originalPrice.subtract(discountAmount);

        item.setHasDiscount(true);
        item.setDiscountedPrice(discountedPrice);
        item.setDiscountLabel(bestDiscount.getName());
    } else {
        item.setHasDiscount(false);
        item.setDiscountedPrice(originalPrice); // fallback
    }

    return discountedPrice;
}

}
