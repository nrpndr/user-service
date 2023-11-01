package com.cineevent.userservice.validators;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cineevent.userservice.exceptions.InValidUserInputException;

public class PasswordValidatorTest {

    private static PasswordValidator passwordValidator;

    @BeforeAll
    public static void setup() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    public void testValidPassword() {
        // Valid passwords
        String[] validPasswords = {
            "Passw0rd!",
            "Strong#123",
            "P@$$w0rd",
            "Abc123!@#"
        };

        for (String password : validPasswords) {
            try {
                passwordValidator.checkPassword(password);
            } catch (InValidUserInputException e) {
                fail("Valid password failed validation: " + password);
            }
        }
    }

    @Test
    public void testInvalidPassword() {
        // Invalid passwords
        String[] invalidPasswords = {
            "weak",           // Too short
            "Weak12345",      // Missing special character
            "Weak@Password",  // Missing number
            "1234567890Aa",   // Missing special character
            "!@#$%^&*()_+Aa", // Missing number
            "TooLongPassword1234567890!",  // Exceeds maximum length
            "",               // Empty string
            null             // Null input
        };

        for (String password : invalidPasswords) {
            try {
                passwordValidator.checkPassword(password);
                fail("Invalid password passed validation: " + password);
            } catch (InValidUserInputException e) {
                // Expected exception
            }
        }
    }

    @Test
    public void testMaxPasswordLength() {
        // Password with the maximum allowed length
        StringBuilder passwordBuilder = new StringBuilder();
        String validPassword = "test@123";
        int validPasswordLength = validPassword.length();
        for (int i = 0; i < PasswordValidator.MAXIMUM_PASSWORD_LENGTH-validPasswordLength; i++) {
            passwordBuilder.append("a");
        }
        passwordBuilder.append(validPassword);

        String maxPassword = passwordBuilder.toString();

        try {
            passwordValidator.checkPassword(maxPassword);
        } catch (InValidUserInputException e) {
            fail("Valid password with max length failed validation");
        }
    }

    @Test
    public void testExceedMaxPasswordLength() {
        // Password exceeding the maximum allowed length
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < PasswordValidator.MAXIMUM_PASSWORD_LENGTH + 1; i++) {
            passwordBuilder.append("a");
        }

        String tooLongPassword = passwordBuilder.toString();

        try {
            passwordValidator.checkPassword(tooLongPassword);
            fail("Password exceeding max length passed validation");
        } catch (InValidUserInputException e) {
            // Expected exception
            assertEquals("Password is too long: max number of chars is 20", e.getMessage());
        }
    }
}