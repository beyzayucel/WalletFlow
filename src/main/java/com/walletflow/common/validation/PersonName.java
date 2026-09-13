package com.walletflow.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PersonNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PersonName {

    String message() default "{validation.person.name.format}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
