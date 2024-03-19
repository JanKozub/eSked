package org.jk.esked.app.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;

public class AdminReturnButton extends Button {

    public AdminReturnButton() {
        setText(getTranslation("return"));
        setWidth("100%");
        addClickListener(event -> UI.getCurrent().navigate("admin"));
    }
}
