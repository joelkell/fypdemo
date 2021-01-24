package com.joelkell.demo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StatusValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusConstraint {
  String message() default "Status must be ORDERED, DELIVERED, CANCELLED, or ON_THE_WAY";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
