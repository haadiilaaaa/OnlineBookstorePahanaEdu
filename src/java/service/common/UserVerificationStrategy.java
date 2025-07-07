// service.common.UserVerificationStrategy.java
package service.common;

public interface UserVerificationStrategy {
    void verify(String userId) throws Exception;
}
