package org.jk.esked.app.frontend.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class RedButton extends Button {
    public RedButton(String text) {
        super(text);
        getStyle().set("color", "red");
    }

    public RedButton(Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(icon, clickListener);
        getStyle().set("color", "red");
    }

    public RedButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, clickListener);
        getStyle().set("color", "red");
    }
}
