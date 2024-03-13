package org.jk.eSked.ui.components.myComponents;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;

import java.util.Locale;

public class TopicField extends TextField {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();

    public TopicField() {
        setPlaceholder(getTranslation(locale, "topic"));
        setWidth("100%");
        setErrorMessage(getTranslation(locale, "exception.empty.field"));
    }
}
