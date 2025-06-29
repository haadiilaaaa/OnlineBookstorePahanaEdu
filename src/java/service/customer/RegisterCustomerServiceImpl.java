package service.customer;

import dao.CustomerDAO;
import dto.CustomerDTO;
import mapper.UserMapper;
import model.Customer;
import service.common.InputValidationService;
import service.common.OtpSendService;
import util.IDGenerator;
import util.PasswordHasher;

public class RegisterCustomerServiceImpl implements RegisterCustomerService {

    private final CustomerDAO customerDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSenderService;

    public RegisterCustomerServiceImpl(CustomerDAO customerDAO,
                                       InputValidationService inputValidationService,
                                       OtpSendService otpSenderService) {
        this.customerDAO = customerDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
    }

    @Override
    public void register(CustomerDTO dto) throws Exception {
        // Validate input
        inputValidationService.validate(dto);

        // Generate ID
        int currentCount = customerDAO.countCustomers();
        String generatedId = IDGenerator.generateId("cus", currentCount);

        // Hash password
        String hashedPassword = PasswordHasher.hashPassword(dto.getPassword());

        // Map to Customer model
        Customer customer = UserMapper.toCustomer(dto, generatedId, hashedPassword);

        // Save to DB
        customerDAO.save(customer);

        // Send OTP
        otpSenderService.sendOtp(customer.getId(), "customer", customer.getEmail());
    }
}
