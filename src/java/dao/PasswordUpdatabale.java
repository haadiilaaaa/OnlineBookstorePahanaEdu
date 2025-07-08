package dao;

public interface PasswordUpdatabale {
    void updatePassword(String userId, String hashedPassword) throws Exception;
}