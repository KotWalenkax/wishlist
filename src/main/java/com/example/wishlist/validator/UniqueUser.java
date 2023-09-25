package com.example.wishlist.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserValidator.class)
@Documented
public @interface UniqueUser {

    String message() default "{UniqueUser.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
