package org.jk.eSked.view.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class TextFieldInput extends HorizontalLayout {
    public TextFieldInput(String name, String data) {
        TextField textField = new TextField(name);
        textField.setPlaceholder(data);

        Button button = new Button("Zmień");

        textField.setWidth("80%");
        button.setWidth("20%");
        button.getStyle().set("margin-top", "auto");
        add(textField, button);
    }
}
