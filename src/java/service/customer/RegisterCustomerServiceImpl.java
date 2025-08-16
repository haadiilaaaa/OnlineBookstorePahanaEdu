package service.customer;

import dao.CustomerDAO;
import dto.CustomerDTO;
import mapper.UserMapper;
import model.Customer;
import service.common.GlobalUserValidator;
import service.common.InputValidationService;
import service.common.OtpSendService;
import util.IDGenerator;
import util.PasswordHasher;

public class RegisterCustomerServiceImpl implements RegisterCustomerService {

    private final CustomerDAO customerDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSenderService;
    private final GlobalUserValidator globalUserValidator;
    private final IDGenerator<String> customerIdGenerator; // New dependency

    public RegisterCustomerServiceImpl(CustomerDAO customerDAO,
                                       InputValidationService inputValidationService,
                                       OtpSendService otpSenderService,
                                       GlobalUserValidator globalUserValidator,
                                       IDGenerator<String> customerIdGenerator) { // Injected here
        this.customerDAO = customerDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
        this.globalUserValidator = globalUserValidator;
        this.customerIdGenerator = customerIdGenerator;
    }

    @Override
    public void register(CustomerDTO dto) throws Exception {
        // Validate input fields
        inputValidationService.validate(dto);

        // Check for global uniqueness of username and email
        globalUserValidator.validateUniqueUsernameAndEmail(dto.getUsername(), dto.getEmail());

        // Use the injected IDGenerator to get the new ID
        String generatedId = customerIdGenerator.generate();
        dto.setId(generatedId);

        // Hash password
        String hashedPassword = PasswordHasher.hashPassword(dto.getPassword());

        // Map to model
        Customer customer = UserMapper.toCustomer(dto, generatedId, hashedPassword);

        // Persist to database
        customerDAO.save(customer);

        // Send OTP for verification
        otpSenderService.sendOtp(customer.getId(), "customer", customer.getEmail());
    }
}