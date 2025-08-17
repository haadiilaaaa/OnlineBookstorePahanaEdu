// File: dao/PasswordResetTokenDAO.java
package dao;

import model.PasswordResetToken;

public interface PasswordResetTokenDAO {
    void save(PasswordResetToken token) throws Exception;
    PasswordResetToken findByToken(String token) throws Exception;
    void markAsUsed(String token) throws Exception;
}   
//hii