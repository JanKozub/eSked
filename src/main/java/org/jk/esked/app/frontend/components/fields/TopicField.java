package org.jk.esked.app.frontend.components.fields;

import com.vaadin.flow.component.textfield.TextField;

public class TopicField extends TextField {
    public TopicField() {
        setPlaceholder(getTranslation("topic"));
        setErrorMessage(getTranslation("exception.empty.field"));
        addClassName("custom-field");
    }
}
