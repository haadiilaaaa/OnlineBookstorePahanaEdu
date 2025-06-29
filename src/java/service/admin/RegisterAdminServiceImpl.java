package service.admin;

import dao.AdminDAO;
import dto.AdminDTO;
import mapper.UserMapper;
import model.Admin;
import service.common.InputValidationService;
import service.common.OtpSendService;
import util.IDGenerator;
import util.PasswordHasher;

public class RegisterAdminServiceImpl implements RegisterServiceAdmin {

    private final AdminDAO adminDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSenderService;

    public RegisterAdminServiceImpl(AdminDAO adminDAO,
                                    InputValidationService inputValidationService,
                                    OtpSendService otpSenderService) {
        this.adminDAO = adminDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
    }

    @Override
    public void register(AdminDTO dto) throws Exception {
        inputValidationService.validate(dto);
        int count = adminDAO.countAdmins();
        String generatedId = IDGenerator.generateId("ad", count);
        String hashedPassword = PasswordHasher.hashPassword(dto.getPassword());
        Admin admin = UserMapper.toAdmin(dto, generatedId, hashedPassword);
        adminDAO.save(admin);
        otpSenderService.sendOtp(admin.getId(), "admin", admin.getEmail());
    }
}
