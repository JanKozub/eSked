package org.jk.esked.app.backend.model.exceptions;


import org.jk.esked.app.backend.model.types.FieldType;

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
