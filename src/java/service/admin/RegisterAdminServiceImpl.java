package service.admin;

import dao.AdminDAO;
import dto.AdminDTO;
import mapper.UserMapper;
import model.Admin;
import service.common.GlobalUserValidator;
import service.common.InputValidationService;
import service.common.OtpSendService;
import util.IDGenerator;
import util.PasswordHasher;

public class RegisterAdminServiceImpl implements RegisterServiceAdmin {

    private final AdminDAO adminDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSenderService;
    private final GlobalUserValidator globalUserValidator;

    public RegisterAdminServiceImpl(AdminDAO adminDAO,
                                    InputValidationService inputValidationService,
                                    OtpSendService otpSenderService,
                                    GlobalUserValidator globalUserValidator) {
        this.adminDAO = adminDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
        this.globalUserValidator = globalUserValidator;
    }

   @Override
public void register(AdminDTO dto) throws Exception {
    // Validate input fields
    inputValidationService.validate(dto);

    // Check uniqueness globally
    globalUserValidator.validateUniqueUsernameAndEmail(dto.getUsername(), dto.getEmail());

    // Generate ID
    int count = adminDAO.countAdmins();
    String generatedId = IDGenerator.generateId("ad", count);
    dto.setId(generatedId); // ✅ CRUCIAL STEP

    // Hash password
    String hashedPassword = PasswordHasher.hashPassword(dto.getPassword());

    // Map DTO to model
    Admin admin = UserMapper.toAdmin(dto, generatedId, hashedPassword);

    // Save to DB
    adminDAO.save(admin);

    // Send OTP
    otpSenderService.sendOtp(admin.getId(), "admin", admin.getEmail());
}
}