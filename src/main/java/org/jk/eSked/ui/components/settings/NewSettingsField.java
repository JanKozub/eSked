package org.jk.eSked.ui.components.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.types.FieldType;

import java.util.Locale;

public abstract class NewSettingsField extends VerticalLayout {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private final TextField textField = new TextField();
    private final Button normalButton = new Button();
    private final Button commitButton = new Button();
    private final HorizontalLayout buttons = new HorizontalLayout();

    public NewSettingsField(FieldType fieldType) {
        Label label = new Label(getTranslation(locale, "field_title_" + fieldType.getDescription()));
        label.getStyle().set("font-size", "var(--lumo-font-size-s)");
        label.getStyle().set("font-weight", "500");
        label.getStyle().set("color", "var(--lumo-secondary-text-color)");
        add(label);

        textField.setReadOnly(true);
        textField.setWidth("70%");
        textField.setPlaceholder(getTranslation(locale, "field_placeholder_" + fieldType.getDescription()));

        normalButton.setText(getTranslation(locale, "change"));
        normalButton.setWidth("30%");
        normalButton.addClickListener(b -> onStartEdit());

        commitButton.setText(getTranslation(locale, "confirm"));
        commitButton.setWidth("40%");
        commitButton.addClickListener(b -> onConfirm());

        buttons.add(textField);
        buttons.setWidth("100%");
        buttons.add(normalButton);

        setPadding(false);
        setSpacing(false);
        add(buttons);
    }

    public void updateMainValue(String value)  {
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

    protected abstract void validateInput(String input);

    protected abstract void commitInput(String input) throws Exception;
}
