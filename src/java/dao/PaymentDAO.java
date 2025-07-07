package dao;

import model.payment;

public interface PaymentDAO {
    void save(payment payment) throws Exception;
}
