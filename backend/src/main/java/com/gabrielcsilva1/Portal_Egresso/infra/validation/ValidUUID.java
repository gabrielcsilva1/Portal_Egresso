package com.gabrielcsilva1.Portal_Egresso.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UUIDValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {
    String message() default "The ID must be a valid UUID.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

