package service.staff;

import dao.StaffDAO;
import dto.StaffDTO;
import mapper.UserMapper;
import model.Staff;
import service.common.InputValidationService;
import service.common.OtpSendService;
import util.IDGenerator;
import util.PasswordHasher;
import service.common.GlobalUserValidator;

public class RegisterStaffServiceImpl implements RegisterStaffService {

    private final StaffDAO staffDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSenderService;
    private final GlobalUserValidator globalUserValidator;
    private final IDGenerator<String> staffIdGenerator; // New dependency

    public RegisterStaffServiceImpl(StaffDAO staffDAO,
                                    InputValidationService inputValidationService,
                                    OtpSendService otpSenderService,
                                    GlobalUserValidator globalUserValidator,
                                    IDGenerator<String> staffIdGenerator) { // Injected here
        this.staffDAO = staffDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
        this.globalUserValidator = globalUserValidator;
        this.staffIdGenerator = staffIdGenerator;
    }

    @Override
    public void register(StaffDTO dto) throws Exception {
        // Validate input fields
        inputValidationService.validate(dto);

        // Check for global uniqueness of username and email
        globalUserValidator.validateUniqueUsernameAndEmail(dto.getUsername(), dto.getEmail());

        // Use the injected IDGenerator to get the new ID
        String generatedId = staffIdGenerator.generate();
        dto.setId(generatedId);

        // Hash password
        String hashedPassword = PasswordHasher.hashPassword(dto.getPassword());

        // Map DTO to model
        Staff staff = UserMapper.toStaff(dto, generatedId, hashedPassword);

        // Save staff to DB
        staffDAO.save(staff);

        // Send OTP
        otpSenderService.sendOtp(staff.getId(), "staff", staff.getEmail());
    }
}