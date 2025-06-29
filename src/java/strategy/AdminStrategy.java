package strategy;

import dao.AdminDAO;
import dto.AdminDTO;
import model.Admin;
import service.common.InputValidationService;
import service.common.OtpSendService;
import util.IDGenerator;
import util.PasswordHasher;
import mapper.UserMapper;

public class AdminStrategy implements RegistrationStrategy {

    private final AdminDAO adminDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSenderService;

    public AdminStrategy(AdminDAO adminDAO,
                         InputValidationService inputValidationService,
                         OtpSendService otpSenderService) {
        this.adminDAO = adminDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
    }

    @Override
    public void register(Object dto) throws Exception {
        if (!(dto instanceof AdminDTO adminDTO)) {
            throw new IllegalArgumentException("Invalid DTO type for AdminStrategy");
        }

        inputValidationService.validate(adminDTO);
        String id = IDGenerator.generateId("ad", adminDAO.countAdmins());
        String hashedPassword = PasswordHasher.hashPassword(adminDTO.getPassword());
        Admin admin = UserMapper.toAdmin(adminDTO, id, hashedPassword);
        adminDAO.save(admin);
        otpSenderService.sendOtp(admin.getId(), "admin", admin.getEmail());
    }
}
