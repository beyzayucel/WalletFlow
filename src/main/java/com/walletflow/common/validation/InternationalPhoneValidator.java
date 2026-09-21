package com.walletflow.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class InternationalPhoneValidator implements ConstraintValidator<InternationalPhone, String> {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^5\\d{9}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        return PHONE_PATTERN.matcher(value).matches();
    }
}