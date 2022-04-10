package com.statista.code.challenge.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = MonetaryAmountValidator.class)
@Documented
public @interface MonetaryVal {
    String message() default "Monetary Amount is not allowed.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
