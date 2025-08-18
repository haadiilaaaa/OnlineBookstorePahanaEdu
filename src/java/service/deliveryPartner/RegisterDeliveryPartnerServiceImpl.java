package service.deliveryPartner;

import dao.DeliveryPartnerDAO;
import dto.DeliveryPartnerDTO;
import mapper.UserMapper;
import model.DeliveryPartner;
import service.common.InputValidationService;
import service.common.OtpSendService;
import service.common.GlobalUserValidator;
import util.IDGenerator;
import util.PasswordHasher;  

public class RegisterDeliveryPartnerServiceImpl implements DeliveryPartnerRegistrationService {
    
    private final DeliveryPartnerDAO deliveryPartnerDAO;
    private final InputValidationService inputValidationService;
    private final OtpSendService otpSendService;
    private final GlobalUserValidator globalValidator;
    private final IDGenerator<String> deliveryPartnerIdGenerator; // New dependency

    public RegisterDeliveryPartnerServiceImpl(
            DeliveryPartnerDAO deliveryPartnerDAO,
            InputValidationService inputValidationService,
            OtpSendService otpSendService,
            GlobalUserValidator globalValidator,
            IDGenerator<String> deliveryPartnerIdGenerator) { // Injected here
        this.deliveryPartnerDAO = deliveryPartnerDAO;
        this.inputValidationService = inputValidationService;
        this.otpSendService = otpSendService;
        this.globalValidator = globalValidator;
        this.deliveryPartnerIdGenerator = deliveryPartnerIdGenerator;
    }

    @Override
    public void register(DeliveryPartnerDTO dto) throws Exception {
        // Validate input fields (nulls, format, password match, etc.)
        inputValidationService.validate(dto);

        // Validate username/email globally across all user types
        globalValidator.validateUniqueUsernameAndEmail(dto.getUsername(), dto.getEmail());

        // Use the injected IDGenerator to get the new ID
        String generatedId = deliveryPartnerIdGenerator.generate();
        dto.setId(generatedId);

        // Hash password
        String hashedPassword = PasswordHasher.hashPassword(dto.getPassword());

        // Map DTO to model
        DeliveryPartner deliveryPartner = UserMapper.toDeliveryPartner(dto, generatedId, hashedPassword);

        // Save to DB
        deliveryPartnerDAO.save(deliveryPartner);

        // Send OTP email
        otpSendService.sendOtp(deliveryPartner.getId(), "delivery", deliveryPartner.getEmail());
    }
}