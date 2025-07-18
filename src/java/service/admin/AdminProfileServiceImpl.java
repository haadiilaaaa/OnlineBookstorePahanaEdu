package service.admin;

import dao.AdminDAO;
import dao.GenericUserDAO;
import dto.AdminDTO;
import model.Admin;
import model.User;
import service.common.AdminValidator;
import service.common.GlobalUserValidator;
import util.DAOExeption;
import util.ValidationException;
import util.PasswordHasher;
import util.*;
import dao.*;

import java.util.List;

public class AdminProfileServiceImpl implements AdminProfileService {

    private final AdminDAO adminDAO;
    private final AdminValidator adminValidator;
    private final GlobalUserValidator globalUserValidator;

    public AdminProfileServiceImpl(AdminDAO adminDAO, List<GenericUserDAO<? extends User>> allUserDAOs) {
        this.adminDAO = adminDAO;
        this.adminValidator = new AdminValidator();
        this.globalUserValidator = new GlobalUserValidator(allUserDAOs);
    }

    @Override
    public AdminDTO getAdminProfile(String adminId) throws DAOExeption {
        Admin admin = adminDAO.findById(adminId)
                .orElseThrow(() -> new DAOExeption("Admin not found"));

        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setEmail(admin.getEmail());
        dto.setContactNumber(admin.getContactNumber());
        return dto;
    }

   @Override
public void updateAdminProfile(AdminDTO dto) throws DAOExeption, ValidationException {
    adminValidator.validateForProfileUpdate(dto);

    try {
        globalUserValidator.validateUniqueUsernameAndEmail(dto.getUsername(), dto.getEmail(), dto.getId());
    } catch (Exception e) {
        throw new ValidationException("Username or email is already taken.");
    }

    Admin admin = new Admin();
    admin.setId(dto.getId());
    admin.setUsername(dto.getUsername());
    admin.setFirstName(dto.getFirstName());
    admin.setLastName(dto.getLastName());
    admin.setEmail(dto.getEmail());
    admin.setContactNumber(dto.getContactNumber());

    if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
        String hashedPassword = PasswordHasher.hashPassword(dto.getPassword());
        admin.setPasswordHash(hashedPassword);

        if (adminDAO instanceof PasswordUpdatabale) {
            try {
                ((PasswordUpdatabale) adminDAO).updatePassword(dto.getId(), hashedPassword);
            } catch (Exception e) {
                throw new DAOExeption("Failed to update password", e);
            }
        }
    }

    adminDAO.update(admin);
}

}
