package org.jk.esked.app.ui.components;

import com.vaadin.flow.component.textfield.TextField;

public class TopicField extends TextField {
    public TopicField() {
        setPlaceholder(getTranslation("topic"));
        setWidth("100%");
        setErrorMessage(getTranslation("exception.empty.field"));
    }
}
