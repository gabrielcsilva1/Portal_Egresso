package com.gabrielcsilva1.Portal_Egresso.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<ValidUUID, UUID> {

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        
        try {
            UUID.fromString(value.toString());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
