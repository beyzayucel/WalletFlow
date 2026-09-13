package com.walletflow.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PersonNameValidator implements ConstraintValidator<PersonName, String> {

    private static final String NAME_REGEX = "^[a-zA-ZçÇğĞıİöÖşŞüÜ]+([ '-][a-zA-ZçÇğĞıİöÖşŞüÜ]+)*$";
    private static final Pattern PATTERN = Pattern.compile(NAME_REGEX);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        return PATTERN.matcher(value.trim()).matches();
    }
}