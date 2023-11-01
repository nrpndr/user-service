package com.cineevent.userservice.validators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cineevent.userservice.exceptions.InValidUserInputException;

public class UserEmailValidatorTest {

    private static UserEmailValidator emailValidator;

    @BeforeAll
    public static void setup() {
        emailValidator = new UserEmailValidator();
    }

    @Test
    public void testValidEmail() {
        // Valid email addresses
        String[] validEmails = {
            "test@example.com",
            "user123@gmail.com",
            "john.doe123@subdomain.domain.co.uk"
        };

        for (String email : validEmails) {
            try {
                emailValidator.checkEmail(email);
            } catch (InValidUserInputException e) {
                fail("Valid email address failed validation: " + email);
            }
        }
    }

    @Test
    public void testInvalidEmail() {
        // Invalid email addresses
        String[] invalidEmails = {
            "invalid-email",       // Missing '@'
            "user@.com",           // Missing domain
            "user@domain.",        // Missing top-level domain
            "@domain.com",         // Missing username
            "user@domain.verylongtopleveldomain",  // Top-level domain too long
            "user@domain.c",       // Top-level domain too short
            "user@domain.co1",     // Top-level domain contains numbers
            "user@domain!.com",    // Invalid character in domain
            "user@domain.com_",    // Underscore in top-level domain
            "user@.com",           // Missing username and domain
            "",                    // Empty string
            null                  // Null input
        };

        for (String email : invalidEmails) {
            try {
                emailValidator.checkEmail(email);
                fail("Invalid email address passed validation: " + email);
            } catch (InValidUserInputException e) {
                // Expected exception
            }
        }
    }

    @Test
    public void testMaxEmailLength() {
        // Email with the maximum allowed length
    	String emailDomain = "@yahoo.com";
    	int emailDomainLength = emailDomain.length();
        StringBuilder emailBuilder = new StringBuilder();
        for (int i = 0; i < UserEmailValidator.MAXIMUM_EMAIL_LENGTH-emailDomainLength; i++) {
            emailBuilder.append("a");
        }
        emailBuilder.append(emailDomain);

        String maxEmail = emailBuilder.toString();

        try {
            emailValidator.checkEmail(maxEmail);
        } catch (InValidUserInputException e) {
            fail("Valid email address with max length failed validation");
        }
    }

    @Test
    public void testExceedMaxEmailLength() {
        // Email with length exceeding the maximum allowed length
        StringBuilder emailBuilder = new StringBuilder();
        for (int i = 0; i < UserEmailValidator.MAXIMUM_EMAIL_LENGTH + 1; i++) {
            emailBuilder.append("a");
        }

        String tooLongEmail = emailBuilder.toString();

        try {
            emailValidator.checkEmail(tooLongEmail);
            fail("Email address exceeding max length passed validation");
        } catch (InValidUserInputException e) {
            // Expected exception
            assertEquals("Email is too long: max number of chars is 100", e.getMessage());
        }
    }
}