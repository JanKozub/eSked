package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import javax.validation.ValidationException;

abstract class SettingsTextField extends VerticalLayout {
    Label label;
    Button button;
    Button commitButton;
    TextField textField;

    private String baseName;
    private String editName;


    protected SettingsTextField() {
        this("Nazwa", "Nowa nazwa");
    }

    protected SettingsTextField(String baseName, String editName) {
        this.baseName = baseName;
        this.editName = editName;

        label = new Label(baseName);
        add(label);

        textField = new TextField();
        textField.setReadOnly(true);
        textField.setWidth("80%");

        button = new Button("Zmień");
        button.setWidth("20%");
        button.addClickListener(this::onStartEdit);

        commitButton = new Button("Potwierdź");
        commitButton.setWidth("20%");
        commitButton.addClickListener(this::onCommit);
        //button.getStyle().set("margin-top", "auto");

        HorizontalLayout buttons = new HorizontalLayout();

        buttons.add(textField);
        buttons.expand(textField);
        buttons.add(button);

        setSpacing(false);
        add(buttons);
    }


    protected void setValue(String value) {
        textField.setValue(value);
    }


    protected void onStartEdit(ClickEvent event) {
        HorizontalLayout parent = (HorizontalLayout) button.getParent().get();
        parent.replace(button, commitButton);

        textField.clear();
        textField.setReadOnly(false);
        textField.setPlaceholder(editName);
    }

    protected void onCommit(ClickEvent event) {
        try {
            validateInput(textField.getValue());
            textField.setInvalid(false);
            commitInput(textField.getValue());
        } catch (ValidationException ex) {
            textField.setErrorMessage(ex.getMessage());
            textField.setInvalid(true);

        }
    }


    protected void validateInput(String input) {
    }

    // SHOULD BE ABSTRACT
    protected void commitInput(String input) {

    }

    protected void completeEdit() {
        textField.setReadOnly(true);

        HorizontalLayout parent = (HorizontalLayout) button.getParent().get();
        parent.replace(commitButton, button);
    }

}
