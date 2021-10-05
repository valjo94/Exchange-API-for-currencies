package com.currency.foreignexchange.validators;

import com.currency.foreignexchange.exceptionhandling.custom.IncorrectDateException;
import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This class is used to implement the constraints for date format validation and throw appropriate exceptions when the validation fails.
 */
public class DateValidator implements ConstraintValidator<DateConstraint, String> {

    @Override
    public void initialize(DateConstraint currency) {
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext cxt) {
        boolean isValid = true;
        if (Strings.isBlank(date)) {
            throw new IncorrectDateException("Date parameter cannot be null");
        }
        if (!date.matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]")) {
            throw new IncorrectDateException("Incorrect date - date format should be yyyy-MM-dd");
        }
        return isValid;
    }

}