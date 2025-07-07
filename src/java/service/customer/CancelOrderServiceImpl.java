
package service.customer;
import dao.*;

public class CancelOrderServiceImpl implements CancelOrderService {
    private final OrderDAO orderDAO;

    public CancelOrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

   public void cancelOrder(String orderId) throws Exception {
    String cancelStatus = "CANCELLED_BY_CUSTOMER"; // match enum exactly
    System.out.println("Updating order " + orderId + " to status " + cancelStatus);
    orderDAO.updateOrderStatus(orderId, cancelStatus);
}

}
