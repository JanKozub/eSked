package org.jk.eSked.ui.components.myComponents;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.server.VaadinSession;

import java.util.Locale;

public class AdminReturnButton extends Button {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();

    public AdminReturnButton() {
        setText(getTranslation(locale, "return"));
        setWidth("100%");
        addClickListener(event -> UI.getCurrent().navigate("admin"));
    }
}
