package org.jk.esked.app.frontend.components.fields;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.FieldType;

public abstract class SettingsField extends VerticalLayout {
    private final TextField textField = new TextField();
    private final Button normalButton;
    private final Button commitButton;
    private final HorizontalLayout buttons = new HorizontalLayout();

    public SettingsField(FieldType fieldType) {
        textField.setReadOnly(true);
        textField.setPlaceholder(getTranslation("field." + fieldType.getDescription() + ".placeholder"));

        normalButton = new Button(getTranslation("change"), b -> onStartEdit());
        normalButton.setWidth("30%");

        commitButton = new Button(getTranslation("confirm"), b -> onConfirm());
        commitButton.setWidth("40%");

        buttons.add(textField, normalButton);

        addClassName("settings-field");
        add(new Span(getTranslation("field." + fieldType.getDescription() + ".title")), buttons);
    }

    public void updateMainValue(String value) {
        textField.setValue(value);
    }

    private void onStartEdit() {
        buttons.replace(normalButton, commitButton);
        textField.clear();
        textField.setReadOnly(false);
        textField.setWidth("60%");
    }

    private void onConfirm() {
        try {
            validateInput(textField.getValue());
            textField.setInvalid(false);
            commitInput(textField.getValue());

            buttons.replace(commitButton, normalButton);
            textField.setReadOnly(true);
            textField.setWidth("70%");
        } catch (Exception ex) {
            textField.setErrorMessage(ex.getMessage());
            textField.setInvalid(true);
        }
    }

    protected abstract void validateInput(String input) throws ValidationException;

    protected abstract void commitInput(String input) throws Exception;
}
