package org.jk.eSked.ui.components.admin;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class InfoBox extends HorizontalLayout {
    public InfoBox(String name, String data) {
        TextField textField = new TextField();
        textField.setValue(name + data);
        textField.setReadOnly(true);
        textField.setWidth("100%");

        add(textField);
        setVerticalComponentAlignment(Alignment.CENTER, textField);
        setWidth("100%");
    }
}
