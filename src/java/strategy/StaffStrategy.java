package strategy;

import dao.StaffDAO;
import dto.StaffDTO;
import model.Staff;
import service.common.InputValidationService;
import service.common.OtpSendService;
import util.IDGenerator;
import util.PasswordHasher;
import mapper.UserMapper;

public class StaffStrategy implements RegistrationStrategy {

    private final StaffDAO staffDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSenderService;

    public StaffStrategy(StaffDAO staffDAO,
                         InputValidationService inputValidationService,
                         OtpSendService otpSenderService) {
        this.staffDAO = staffDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
    }

    @Override
    public void register(Object dto) throws Exception {
        if (!(dto instanceof StaffDTO staffDTO)) {
            throw new IllegalArgumentException("Invalid DTO type for StaffStrategy");
        }

        inputValidationService.validate(staffDTO);
        String id = IDGenerator.generateId("st", staffDAO.countStaff());
        String hashedPassword = PasswordHasher.hashPassword(staffDTO.getPassword());
        Staff staff = UserMapper.toStaff(staffDTO, id, hashedPassword);
        staffDAO.save(staff);
        otpSenderService.sendOtp(staff.getId(), "staff", staff.getEmail());
    }
}
