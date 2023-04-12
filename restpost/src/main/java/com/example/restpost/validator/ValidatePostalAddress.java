package com.example.restpost.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = AddressPostalValidator.class)
public @interface ValidatePostalAddress {
    String message() default "wrong postal code format for the given country";

    Class<?>[] groups() default {};

    Class<? extends Payload> [] payload() default {};

}
