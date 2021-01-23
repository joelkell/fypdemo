package com.joelkell.demo.validation;

import org.bson.types.ObjectId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ObjectIdValidator implements ConstraintValidator<ObjectIdConstraint, ObjectId> {
    @Override
    public void initialize(ObjectIdConstraint objectIdConstraint) {}

    @Override
    public boolean isValid(ObjectId objectId, ConstraintValidatorContext cxt) {
        if (objectId != null) {
            String objectIdString = objectId.toString();
            if (ObjectId.isValid(objectIdString)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}