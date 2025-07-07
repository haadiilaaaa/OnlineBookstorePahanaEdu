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

    public RegisterStaffServiceImpl(StaffDAO staffDAO,
                                    InputValidationService inputValidationService,
                                    OtpSendService otpSenderService,
                                    GlobalUserValidator globalUserValidator) {
        this.staffDAO = staffDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
        this.globalUserValidator = globalUserValidator;
    }

    @Override
    public void register(StaffDTO dto) throws Exception {
        // Validate input fields
        inputValidationService.validate(dto);

        // Check for global uniqueness of username and email
       

        // Generate ID
        int count = staffDAO.countStaff();
        String generatedId = IDGenerator.generateId("st", count);
        dto.setId(generatedId); // ✅ CRUCIAL STEP

        // Hash password
        String hashedPassword = PasswordHasher.hashPassword(dto.getPassword());

        // Map DTO to model
        Staff staff = UserMapper.toStaff(dto, generatedId, hashedPassword);
        globalUserValidator.validateUniqueUsernameAndEmail(dto.getUsername(), dto.getEmail());


        // Save staff to DB
        staffDAO.save(staff);

        // Send OTP
        otpSenderService.sendOtp(staff.getId(), "staff", staff.getEmail());
    }
}
