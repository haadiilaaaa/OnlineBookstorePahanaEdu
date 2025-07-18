package service.admin;

import dto.CustomerDTO;
import util.DAOExeption;

import java.util.List;

public interface CustomerManagemetService {

    List<CustomerDTO> getAllCustomers() throws DAOExeption;

    boolean deleteCustomerById(String customerId) throws DAOExeption;

    CustomerDTO getCustomerById(String customerId) throws DAOExeption;

    boolean updateCustomer(CustomerDTO customerDTO) throws DAOExeption;
}
