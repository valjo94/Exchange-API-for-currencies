package com.currency.foreignexchange.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Constraint annotation used to validate date format.
 */
@Documented
@Constraint(validatedBy = DateValidator.class)
@Target( { ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateConstraint {
    String message() default "Invalid date!!!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

