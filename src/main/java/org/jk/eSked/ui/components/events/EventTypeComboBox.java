package org.jk.eSked.ui.components.events;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.types.EventType;

import java.util.Locale;

public class EventTypeComboBox extends ComboBox<EventType> {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();

    public EventTypeComboBox() {
        setItems(EventType.values());
        setAllowCustomValue(false);
        setRenderer(new TextRenderer<>(EventType::getDescription));
        setPlaceholder(getTranslation(locale, "type"));
        setItemLabelGenerator(EventType::getDescription);
        setWidth("100%");
        setErrorMessage(getTranslation(locale, "exception_empty_field"));
    }
}
