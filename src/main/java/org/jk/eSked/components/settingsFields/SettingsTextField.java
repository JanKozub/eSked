package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import javax.validation.ValidationException;

abstract class SettingsTextField extends VerticalLayout {
    private Button button;
    private Button commitButton;
    TextField textField;

    private String editName;

    SettingsTextField(String baseName, String editName) {
        this.editName = editName;

        Label label = new Label(baseName);
        label.getStyle().set("font-size", "var(--lumo-font-size-s)");
        label.getStyle().set("font-weight", "500");
        label.getStyle().set("color", "var(--lumo-secondary-text-color)");
        add(label);

        textField = new TextField();
        textField.setReadOnly(true);
        textField.setWidth("70%");

        button = new Button("Zmień");
        button.setWidth("30%");
        button.addClickListener(this::onStartEdit);

        commitButton = new Button("Potwierdź");
        commitButton.setWidth("40%");
        commitButton.addClickListener(this::onCommit);

        HorizontalLayout buttons = new HorizontalLayout();

        buttons.add(textField);
        buttons.setWidth("100%");
        buttons.add(button);

        setPadding(false);
        setSpacing(false);
        add(buttons);
    }


    protected void setValue(String value) {
        textField.setValue(value);
    }


    private void onStartEdit(ClickEvent event) {
        HorizontalLayout parent = (HorizontalLayout) button.getParent().get();
        parent.replace(button, commitButton);

        textField.setWidth("60%");
        textField.clear();
        textField.setReadOnly(false);
        textField.setPlaceholder(editName);
    }

    private void onCommit(ClickEvent event) {
        try {
            validateInput(textField.getValue());
            textField.setInvalid(false);
            commitInput(textField.getValue());
        } catch (ValidationException ex) {
            textField.setErrorMessage(ex.getMessage());
            textField.setInvalid(true);

        }
    }

    protected abstract void validateInput(String input);

    protected abstract void commitInput(String input);

    void completeEdit() {
        textField.setWidth("70%");
        textField.setReadOnly(true);

        HorizontalLayout parent = (HorizontalLayout) commitButton.getParent().get();
        parent.replace(commitButton, button);
    }

}
