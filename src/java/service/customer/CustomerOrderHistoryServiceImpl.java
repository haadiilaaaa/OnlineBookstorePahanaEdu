package service.customer;

import dao.OrderDAO;
import dto.OrderDTO;
import java.util.List;

public class CustomerOrderHistoryServiceImpl implements CustomerOrderHistoryService {

    private final OrderDAO orderDAO;

    public CustomerOrderHistoryServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public List<OrderDTO> getOrdersByCustomer(String customerId) throws Exception {
        return orderDAO.findOrdersByCustomerId(customerId);
    }
}
