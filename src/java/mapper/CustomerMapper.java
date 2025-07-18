package mapper;

import dto.CustomerDTO;
import model.Customer;

public class CustomerMapper {

    public static CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setUsername(customer.getUsername());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setContactNumber(customer.getContactNumber());
        dto.setAddress(customer.getAddress());
        return dto;
    }

    public static Customer toEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setUsername(dto.getUsername());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setContactNumber(dto.getContactNumber());
        customer.setAddress(dto.getAddress());
        // passwordHash and verification should be handled separately
        return customer;
    }
}
