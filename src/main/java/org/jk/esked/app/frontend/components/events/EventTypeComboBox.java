package org.jk.esked.app.frontend.components.events;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.jk.esked.app.backend.model.types.EventType;

public class EventTypeComboBox extends ComboBox<EventType> {

    public EventTypeComboBox() {
        setItems(EventType.values());
        setAllowCustomValue(false);
        setRenderer(new TextRenderer<>(eventType -> getTranslation(eventType.getDescription())));
        setPlaceholder(getTranslation("type"));
        setItemLabelGenerator(eventType -> getTranslation(eventType.getDescription()));
        setWidth("100%");
        setErrorMessage(getTranslation("exception.empty.field"));
    }
}
