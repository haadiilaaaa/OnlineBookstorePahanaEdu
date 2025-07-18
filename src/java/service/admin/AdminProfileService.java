package service.admin;

import dto.AdminDTO;
import util.DAOExeption;
import util.ValidationException;

public interface AdminProfileService {
    AdminDTO getAdminProfile(String adminId) throws DAOExeption;
    void updateAdminProfile(AdminDTO dto) throws DAOExeption, ValidationException;
}
