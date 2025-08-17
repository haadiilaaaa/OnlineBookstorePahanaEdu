package service.customer;

import dao.CustomerDAO;
import dao.CustomerDAOimpl;
import db.DBConnection;
import dto.CustomerDTO;
import mapper.CustomerDTOToModelMapper;
import model.Customer;
import service.common.EditProfileService;
import service.common.CustomerValidator;
import util.DAOExeption;
import util.PasswordHasher;
import util.ServiceException;   
import util.ValidationException;
import java.sql.SQLException;

import java.sql.Connection;

public class CustomerEditProfileService implements EditProfileService<CustomerDTO> {

    private final CustomerDAO customerDAO;
    private final CustomerValidator validator;
    private final CustomerDTOToModelMapper mapper;

    public CustomerEditProfileService() {
    try {
        Connection conn = DBConnection.getInstance().getConnection();
        this.customerDAO = new CustomerDAOimpl(conn);
    } catch (SQLException e) {
        throw new RuntimeException("Failed to initialize database connection for CustomerEditProfileService", e);
    }
    this.validator = new CustomerValidator();
    this.mapper = new CustomerDTOToModelMapper();
}


    @Override
    public void updateProfile(CustomerDTO dto) throws ServiceException, ValidationException {
        validator.validateForEdit(dto);

        Customer customer = mapper.toModel(dto);

        String newPasswordHash = null;
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            newPasswordHash = PasswordHasher.hashPassword(dto.getPassword()); // ✅ use your utility
        }

        try {
            customerDAO.updateProfile(customer, newPasswordHash);
        } catch (DAOExeption e) {
            throw new ServiceException("Failed to update customer profile", e);
        }
    }
}
