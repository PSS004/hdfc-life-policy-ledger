package com.hdfclife.ledger.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class PolicyTypeValidator
        implements ConstraintValidator<ValidPolicyType, String> {

    private static final Set<String> VALID_TYPES =
            Set.of("TERM", "ULIP", "ENDOWMENT");

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context) {

        return value != null && VALID_TYPES.contains(value);
    }
}
