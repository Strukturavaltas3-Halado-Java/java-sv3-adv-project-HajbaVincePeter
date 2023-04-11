package com.example.restpost.validator;

import com.example.restpost.dtos.address_commands.UpdateCommand;
import com.example.restpost.dtos.address_commands.UpdatePostalCommand;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AddressPostalValidator implements ConstraintValidator<ValidatePostalAddress, UpdateCommand> {


    @Override
    public void initialize(ValidatePostalAddress constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateCommand updateCommand, ConstraintValidatorContext constraintValidatorContext) {

       return  ((UpdatePostalCommand)updateCommand).getCountry().getPostalCodeFormat() ==
        ((UpdatePostalCommand)updateCommand).getPostalCode().length();

       
    }

}
