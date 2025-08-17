package strategy.payment;

import dto.OrderDTO;

public interface PaymentStrategy {
    void processPayment(OrderDTO order) throws Exception;
}   
