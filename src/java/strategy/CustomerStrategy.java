package strategy;

import dao.CustomerDAO;
import dto.CustomerDTO;
import model.Customer;
import service.common.InputValidationService;
import service.common.OtpSendService;
import util.IDGenerator;
import util.PasswordHasher;
import mapper.UserMapper;

public class CustomerStrategy implements RegistrationStrategy {

    private final CustomerDAO customerDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSenderService;

    public CustomerStrategy(CustomerDAO customerDAO,
                            InputValidationService inputValidationService,
                            OtpSendService otpSenderService) {
        this.customerDAO = customerDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
    }

    @Override
public void register(Object dto) throws Exception {
    try {
        System.out.println("CustomerStrategy - register() started");

        if (!(dto instanceof CustomerDTO customerDTO)) {
            throw new IllegalArgumentException("Invalid DTO type for CustomerStrategy");
        }

        System.out.println("DTO received: " + customerDTO.getEmail());
        inputValidationService.validate(customerDTO);
        System.out.println("Validation successful");

        String id = IDGenerator.generateId("cus", customerDAO.countCustomers());
        customerDTO.setId(id);
        System.out.println("Generated ID: " + id);

        String hashedPassword = PasswordHasher.hashPassword(customerDTO.getPassword());
        System.out.println("Password hashed");

        Customer customer = UserMapper.toCustomer(customerDTO, id, hashedPassword);
        System.out.println("Mapped to Customer model");

        customerDAO.save(customer);
        System.out.println("Customer saved");

        otpSenderService.sendOtp(customer.getId(), "customer", customer.getEmail());
        System.out.println("OTP sent");
    } catch (Exception e) {
        System.err.println("Registration failed: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}

}
