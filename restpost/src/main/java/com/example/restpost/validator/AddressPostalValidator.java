package com.example.restpost.validator;

import com.example.restpost.dtos.address_commands.UpdateCommand;
import com.example.restpost.dtos.address_commands.UpdatePostalCommand;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AddressPostalValidator implements ConstraintValidator<ValidatePostalAddress, UpdatePostalCommand> {


    @Override
    public void initialize(ValidatePostalAddress constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdatePostalCommand updateCommand, ConstraintValidatorContext constraintValidatorContext) {
       if(updateCommand.getCountry() == null) {
           return false;
       }
       if(updateCommand.getPostalCode()==null ) {
           return false;
       }
       return  updateCommand.getCountry().getPostalCodeFormat() ==
        updateCommand.getPostalCode().length();
    }

}
