package org.jk.esked.app.frontend.components.admin;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class InfoBox extends HorizontalLayout {
    public InfoBox(String value) {
        TextField textField = new TextField(value);
        textField.setReadOnly(true);

        add(textField);
        addClassName("info-box");
    }
}
