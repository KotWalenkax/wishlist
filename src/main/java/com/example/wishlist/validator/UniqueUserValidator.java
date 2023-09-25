package com.example.wishlist.validator;

import com.example.wishlist.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String> {

    private final UserService userService;

    public UniqueUserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        } else {
            if (userService.isUserExists(value)) {
                return false;
            } else {
                return true;
            }
        }
    }
}
