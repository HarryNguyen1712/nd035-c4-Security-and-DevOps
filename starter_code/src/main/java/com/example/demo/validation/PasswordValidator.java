package com.example.demo.validation;

import com.example.demo.model.requests.CreateUserRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator
    implements ConstraintValidator<PasswordConstraint, CreateUserRequest> {
  @Override
  public void initialize(PasswordConstraint passwordConstraint) {}

  @Override
  public boolean isValid(
      CreateUserRequest createUserRequest, ConstraintValidatorContext constraintValidatorContext) {
    return createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword());
  }
}
