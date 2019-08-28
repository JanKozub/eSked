package org.jk.eSked.view.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;

public class PasswordFieldInput extends HorizontalLayout {
    public PasswordFieldInput(String name, String data) {
        PasswordField textField = new PasswordField(name);
        textField.setValue(data);

        Button button = new Button("Zmień");

        textField.setWidth("80%");
        button.setWidth("20%");
        button.getStyle().set("margin-top", "auto");
        add(textField, button);
    }
}
