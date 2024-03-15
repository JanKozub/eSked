package org.jk.eSked.ui.components.settings.fields;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.backend.model.exceptions.ValidationException;
import org.jk.eSked.backend.model.types.FieldType;

public abstract class SettingsField extends VerticalLayout {
    private final TextField textField = new TextField();
    private final Button normalButton = new Button();
    private final Button commitButton = new Button();
    private final HorizontalLayout buttons = new HorizontalLayout();

    public SettingsField(FieldType fieldType) {
        Text label = new Text(getTranslation("field." + fieldType.getDescription() + ".title"));
        label.getStyle().set("font-size", "var(--lumo-font-size-s)");
        label.getStyle().set("font-weight", "500");
        label.getStyle().set("color", "var(--lumo-secondary-text-color)");
        add(label);

        textField.setReadOnly(true);
        textField.setWidth("70%");
        textField.setPlaceholder(getTranslation("field." + fieldType.getDescription() + ".placeholder"));

        normalButton.setText(getTranslation("change"));
        normalButton.setWidth("30%");
        normalButton.addClickListener(b -> onStartEdit());

        commitButton.setText(getTranslation("confirm"));
        commitButton.setWidth("40%");
        commitButton.addClickListener(b -> onConfirm());

        buttons.add(textField);
        buttons.setWidth("100%");
        buttons.add(normalButton);

        setPadding(false);
        setSpacing(false);
        add(buttons);
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
