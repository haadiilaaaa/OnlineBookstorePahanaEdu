package service.staff;

import dao.StaffDAO;
import dto.StaffDTO;
import mapper.UserMapper;
import model.Staff;
import service.common.InputValidationService;
import service.common.OtpSendService;
import util.IDGenerator;
import util.PasswordHasher;

public class RegisterStaffServiceImpl implements RegisterStaffService {

    private final StaffDAO staffDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSenderService;

    public RegisterStaffServiceImpl(StaffDAO staffDAO,
                                    InputValidationService inputValidationService,
                                    OtpSendService otpSenderService) {
        this.staffDAO = staffDAO;
        this.inputValidationService = inputValidationService;
        this.otpSenderService = otpSenderService;
    }

    @Override
    public void register(StaffDTO dto) throws Exception {
        inputValidationService.validate(dto);
        int count = staffDAO.countStaff();
        String generatedId = IDGenerator.generateId("st", count);
        String hashedPassword = PasswordHasher.hashPassword(dto.getPassword());
        Staff staff = UserMapper.toStaff(dto, generatedId, hashedPassword);
        staffDAO.save(staff);
        otpSenderService.sendOtp(staff.getId(), "staff", staff.getEmail());
    }
}
