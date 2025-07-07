package service.customer;

import dao.CustomerDAO;
import dto.CustomerDTO;
import mapper.UserMapper;
import model.Customer;
import service.common.InputValidationService;
import service.common.OtpSendService;
import util.IDGenerator;
import util.PasswordHasher;
import service.common.GlobalUserValidator;

public class RegisterCustomerServiceImpl implements RegisterCustomerService {

    private final CustomerDAO customerDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSenderService;
    private final GlobalUserValidator globalUserValidator;

    public RegisterCustomerServiceImpl(CustomerDAO customerDAO,
                                       InputValidationService inputValidationService,
                                       OtpSendService otpSenderService,
                                       GlobalUserValidator globalUserValidator) {
        this.customerDAO = customerDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
        this.globalUserValidator = globalUserValidator;
    }

    @Override
    public void register(CustomerDTO dto) throws Exception {
        // Validate input fields
        inputValidationService.validate(dto);

        // Check for global uniqueness of username and email
       

        // Generate ID
        int currentCount = customerDAO.countCustomers();
        String generatedId = IDGenerator.generateId("cus", currentCount);
        dto.setId(generatedId); // ✅ CRUCIAL STEP

        // Hash password
        String hashedPassword = PasswordHasher.hashPassword(dto.getPassword());

        // Map to model
        Customer customer = UserMapper.toCustomer(dto, generatedId, hashedPassword);
 globalUserValidator.validateUniqueUsernameAndEmail(dto.getUsername(), dto.getEmail());
        // Persist to database
        customerDAO.save(customer);

        // Send OTP for verification
        otpSenderService.sendOtp(customer.getId(), "customer", customer.getEmail());
    }
}
