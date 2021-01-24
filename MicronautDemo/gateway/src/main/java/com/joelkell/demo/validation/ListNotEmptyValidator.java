package com.joelkell.demo.validation;


import com.joelkell.demo.services.orders.OrderItem;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ListNotEmptyValidator
    implements ConstraintValidator<ListNotEmptyConstraint, List<OrderItem>> {

  @Override
  public void initialize(ListNotEmptyConstraint listNotEmptyConstraint) {}

  @Override
  public boolean isValid(List<OrderItem> items, ConstraintValidatorContext cxt) {
    if (items == null || items.size() <= 0) {
      return false;
    } else {
      return true;
    }
  }
}
