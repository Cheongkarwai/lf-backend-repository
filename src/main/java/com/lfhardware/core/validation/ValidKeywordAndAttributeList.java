package com.lfhardware.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = KeywordAndAttributeListValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidKeywordAndAttributeList {
    String message() default "If attributeList is not empty, keyword must not be null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}