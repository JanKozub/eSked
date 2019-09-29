package org.jk.eSked.ui.components.settings;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

abstract class SettingsTextField extends VerticalLayout {
    private final String baseName;
    private final String editName;
    TextField textField;
    private Button button;
    private Button commitButton;

    SettingsTextField(String baseName, String editName) {
        this.baseName = baseName;
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

    private void onStartEdit(ClickEvent event) {
        HorizontalLayout parent = (HorizontalLayout) button.getParent().orElse(null);
        assert parent != null;
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
        } catch (Exception ex) {
            textField.setErrorMessage(ex.getMessage());
            textField.setInvalid(true);
        }
    }

    protected abstract void validateInput(String input);

    protected abstract void commitInput(String input) throws Exception;

    void completeEdit(String data) {
        removeAll();

        Label label = new Label(baseName);
        label.getStyle().set("font-size", "var(--lumo-font-size-s)");
        label.getStyle().set("font-weight", "500");
        label.getStyle().set("color", "var(--lumo-secondary-text-color)");
        add(label);

        textField = new TextField();
        textField.setReadOnly(true);
        textField.setValue(data);
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

}
