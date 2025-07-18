// service/common/EditProfileService.java
package service.common;

import util.ServiceException;
import util.ValidationException;

public interface EditProfileService<T> {
    void updateProfile(T dto) throws ServiceException, ValidationException;
}
