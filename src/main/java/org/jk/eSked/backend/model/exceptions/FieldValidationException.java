package org.jk.eSked.backend.model.exceptions;

import org.jk.eSked.backend.model.types.FieldType;

public class FieldValidationException extends ValidationException {
    private final FieldType fieldType;

    public FieldValidationException(String message, FieldType fieldType) {
        super(message);
        this.fieldType = fieldType;
    }

    public FieldType getFieldType() {
        return fieldType;
    }
}
