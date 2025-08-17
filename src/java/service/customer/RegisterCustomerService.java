package service.customer;

import dto.CustomerDTO;

public interface RegisterCustomerService {
    void register(CustomerDTO dto) throws Exception;
}  
