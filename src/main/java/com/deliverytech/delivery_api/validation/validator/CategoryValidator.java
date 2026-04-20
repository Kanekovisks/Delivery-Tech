package com.deliverytech.delivery_api.validation.validator;

import com.deliverytech.delivery_api.enums.RestaurantCategory;
import com.deliverytech.delivery_api.validation.ValidCategory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryValidator implements ConstraintValidator<ValidCategory, String> {
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            RestaurantCategory.valueOf(value.toUpperCase());
            return true;
        } catch(IllegalArgumentException exception) {
            return false;
        }
    }
}
