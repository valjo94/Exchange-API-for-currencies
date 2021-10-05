package com.currency.foreignexchange.validators;

import com.currency.foreignexchange.exceptionhandling.custom.CurrencyCodeException;
import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Currency;
import java.util.Locale;

/**
 * This class is used to implement the constraints for the currency codes and throw appropriate exceptions when the validation fails.
 */
public class CurrencyValidator implements ConstraintValidator<CurrencyConstraint, String> {

    @Override
    public void initialize(CurrencyConstraint currency) {
    }

    @Override
    public boolean isValid(String currencyCode, ConstraintValidatorContext cxt) {
        String parameterName = cxt.getDefaultConstraintMessageTemplate();
        if (Strings.isBlank(currencyCode)) {
            throw new CurrencyCodeException("Currency code parameter '" + parameterName + "' not specified.");
        }

        try {
            return Currency.getAvailableCurrencies().contains(Currency.getInstance(currencyCode.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ex) {
            throw new CurrencyCodeException("Request currency code parameter '" + parameterName + "' is invalid.");
        }
    }

}