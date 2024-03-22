package org.jk.esked.app.frontend.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class GreenButton extends Button {
    public GreenButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, clickListener);
        getStyle().set("color", "green");
    }
}
