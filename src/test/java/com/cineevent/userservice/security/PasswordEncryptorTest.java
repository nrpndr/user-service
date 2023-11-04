package com.cineevent.userservice.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PasswordEncryptorTest {

    @Test
    public void testGetEncryptedPassword() {
        String password = "password123";
        String encryptedPassword = PasswordEncryptor.getEncryptedPassword(password);

        assertNotNull(encryptedPassword);
        assertNotEquals(password, encryptedPassword);
    }

    @Test
    public void testIsPasswordValidWithValidPassword() {
        String password = "password123";
        String encryptedPassword = PasswordEncryptor.getEncryptedPassword(password);

        assertTrue(PasswordEncryptor.isPasswordValid(password, encryptedPassword));
    }

    @Test
    public void testIsPasswordValidWithInvalidPassword() {
        String password = "password123";
        String incorrectPassword = "incorrectPassword";
        String encryptedPassword = PasswordEncryptor.getEncryptedPassword(password);

        assertFalse(PasswordEncryptor.isPasswordValid(incorrectPassword, encryptedPassword));
    }

    @Test
    public void testIsPasswordValidWithNullInput() {
        String password = "password123";
        String encryptedPassword = PasswordEncryptor.getEncryptedPassword(password);

        assertFalse(PasswordEncryptor.isPasswordValid(null, encryptedPassword));
    }

    @Test
    public void testIsPasswordValidWithNullPasswordInDB() {
        String password = "password123";
        
        assertFalse(PasswordEncryptor.isPasswordValid(password, null));
    }
}
