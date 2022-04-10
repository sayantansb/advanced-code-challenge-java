package com.statista.code.challenge.validators;

import com.statista.code.challenge.domain.CurrencyTypes;
import lombok.extern.slf4j.Slf4j;

import javax.money.MonetaryAmount;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class MonetaryAmountValidator  implements ConstraintValidator<MonetaryVal, MonetaryAmount> {
    @Override
    public boolean isValid(MonetaryAmount monetaryAmount, ConstraintValidatorContext constraintValidatorContext) {
        log.info("In MonetaryAmountValidator, monetaryAmount = {}",monetaryAmount);
        if(monetaryAmount.getCurrency()==null || CurrencyTypes.valueOf(monetaryAmount.getCurrency().getCurrencyCode()) == null){
            return false;
        }else if(monetaryAmount.getNumber()==null || monetaryAmount.getNumber().doubleValue() <= 0){
            return false;
        }
        return true;
    }
}
