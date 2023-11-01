package com.cineevent.userservice.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.cineevent.userservice.exceptions.InValidUserInputException;

@Component
public class PasswordValidator {

    public static final int MAXIMUM_PASSWORD_LENGTH = 20;

    private static final String USER_PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

    private Pattern pattern;

    public PasswordValidator() {
        pattern = Pattern.compile(USER_PASSWORD_REGEX);
    }

    public void checkPassword(String password) {
        if (Strings.isBlank(password)) {
            throw new InValidUserInputException("Password cannot be null or empty");
        }

        // check max length
        if (password.length() > MAXIMUM_PASSWORD_LENGTH) {
            throw new InValidUserInputException(String.format("Password is too long: max number of chars is %s",
            		MAXIMUM_PASSWORD_LENGTH));
        }

        // Minimum eight characters, at least one letter, one number and one special character
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new InValidUserInputException("Password must be minimum eight characters, at least one letter, one number and one special character");
        }
    }

}
