package mapper;

import dto.CustomerDTO;
import model.Customer;

public class CustomerDTOToModelMapper {
    public Customer toModel(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setUsername(dto.getUsername());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setContactNumber(dto.getContactNumber());
        customer.setAddress(dto.getAddress());
        // passwordHash should only be set if updating password separately
        return customer;
    }
}
