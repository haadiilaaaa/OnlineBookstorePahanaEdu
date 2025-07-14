// service/common/ResetPasswordService.java
package service.common;

public interface ResetPasswordService {
    boolean resetPassword(String token, String newPassword, String confirmPassword) throws Exception;
}
