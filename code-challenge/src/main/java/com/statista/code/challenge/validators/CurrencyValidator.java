package com.statista.code.challenge.validators;

import com.statista.code.challenge.domain.CurrencyTypes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * Purpose of this validator is to reject requests for Booking creation/modification if the currency is not listed in CurrencyTypes enum
 */

public class CurrencyValidator implements ConstraintValidator<CurrencyVal, CurrencyTypes> {

    List<String> currencies = Arrays.asList("USD","EUR","GBP","JPY","CHF","AUD","CAD","SGD","INR","HKD");

    @Override
    public boolean isValid(CurrencyTypes s, ConstraintValidatorContext constraintValidatorContext) {
        return currencies.contains(s.name());
    }
}
