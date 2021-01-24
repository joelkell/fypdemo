package com.joelkell.demo.validation;

import com.joelkell.demo.services.orders.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<StatusConstraint, Status> {

  @Override
  public void initialize(StatusConstraint statusConstraint) {}

  @Override
  public boolean isValid(Status status, ConstraintValidatorContext cxt) {
    if (status != null) {
      for (Status i : Status.values()) {
        if (i.name().equals(status.name())) {
          return true;
        }
      }
    }
    return false;
  }
}
