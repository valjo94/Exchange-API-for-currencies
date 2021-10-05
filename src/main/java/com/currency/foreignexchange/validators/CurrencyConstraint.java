package com.currency.foreignexchange.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Constraint annotation used to validate currency codes.
 */
@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target( { ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyConstraint {
    String message() default "Invalid currency. Please specify a valid currency code.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

