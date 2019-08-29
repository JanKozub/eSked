package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

class SettingsTextField extends HorizontalLayout {
    Button button;
    TextField textField;

    SettingsTextField() {
        textField = new TextField();
        textField.setReadOnly(true);

        button = new Button("Zmień");
        textField.setWidth("80%");
        button.setWidth("20%");
        button.getStyle().set("margin-top", "auto");
        add(textField, button);
    }
}
