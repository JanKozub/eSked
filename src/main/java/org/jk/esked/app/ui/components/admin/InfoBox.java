package org.jk.esked.app.ui.components.admin;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class InfoBox extends HorizontalLayout {
    public InfoBox(String value) {
        TextField textField = new TextField();
        textField.setValue(value);
        textField.setReadOnly(true);
        textField.setWidth("100%");

        add(textField);
        setVerticalComponentAlignment(Alignment.CENTER, textField);
        setWidth("100%");
    }
}
