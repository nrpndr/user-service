package com.cineevent.userservice.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.cineevent.userservice.exceptions.InValidUserInputException;

@Component
public class UserEmailValidator {

    private static final int MAXIMUM_EMAIL_LENGTH = 100;

    private static final String USER_EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private Pattern pattern;

    public UserEmailValidator() {
        pattern = Pattern.compile(USER_EMAIL_REGEX);
    }

    public void checkEmail(String email) {
    	
        if (Strings.isBlank(email)) {
            throw new InValidUserInputException("Email cannot be null or empty");
        }

        // check the maximum 
        if (email.length() > MAXIMUM_EMAIL_LENGTH) {
            throw new InValidUserInputException(String.format("Email is too long: max number of chars is %s",
            		MAXIMUM_EMAIL_LENGTH));
        }

        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new InValidUserInputException(String.format("Email provided %s is not of valid formal", email));
        }
    }

}
