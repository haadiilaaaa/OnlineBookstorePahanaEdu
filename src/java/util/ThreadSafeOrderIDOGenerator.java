package util;

import dao.OrderDAO;
import dto.OrderDTO; // Assuming OrderDTO is the type returned by OrderDAO
import java.util.List;
import java.util.stream.Collectors;

public class ThreadSafeOrderIDOGenerator implements IDGenerator<String> {

    private final String prefix;
    private final OrderDAO orderDAO;

    public ThreadSafeOrderIDOGenerator(String prefix, OrderDAO orderDAO) {
        this.prefix = prefix;
        this.orderDAO = orderDAO;
    }

    @Override
    public synchronized String generate() {
        try {
            // Change the type to List<OrderDTO> if that's what findAll() returns
            List<OrderDTO> allOrders = orderDAO.findAll();
            
            int maxNumber = allOrders.stream()
                // Change this method reference to the correct one for OrderDTO
                .map(OrderDTO::getOrderId) 
                .filter(id -> id.startsWith(prefix))
                .map(id -> id.substring(prefix.length()))
                .mapToInt(id -> {
                    try {
                        return Integer.parseInt(id);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);

            return String.format("%s%03d", prefix, maxNumber + 1);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate unique order ID", e);
        }
    }
}