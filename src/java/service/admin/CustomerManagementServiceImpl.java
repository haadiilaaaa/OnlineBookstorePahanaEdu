package service.admin;

import dao.CustomerDAO;
import dto.CustomerDTO;
import mapper.CustomerMapper;
import model.Customer;
import util.DAOExeption;
   
import java.util.List;
import java.util.stream.Collectors;

public class CustomerManagementServiceImpl implements CustomerManagemetService {

    private final CustomerDAO customerDAO;

    public CustomerManagementServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() throws DAOExeption {
        List<Customer> customers = customerDAO.findAll();
        return customers.stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteCustomerById(String customerId) throws DAOExeption {
        return customerDAO.deleteById(customerId);
    }

    @Override
    public CustomerDTO getCustomerById(String customerId) throws DAOExeption {
        return customerDAO.findById(customerId)
                .map(CustomerMapper::toDTO)
                .orElse(null);
    }

    @Override
    public boolean updateCustomer(CustomerDTO customerDTO) throws DAOExeption {
        Customer customer = CustomerMapper.toEntity(customerDTO);
        return customerDAO.update(customer);
    }
}
