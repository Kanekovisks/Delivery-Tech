package com.deliverytech.delivery_api.validation.validator;

import com.deliverytech.delivery_api.validation.ValidPhone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private static final String REGEX = "\\(?\\d{2}\\)?[\\s-]?\\d{4,5}\\-?\\d{4}";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return false;          
        return value.matches(REGEX);
    }
}