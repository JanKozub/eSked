package org.jk.eSked.ui.components.myImpl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class LongSuccessNotification extends Notification {

    public LongSuccessNotification(String text) {
        super(text);
        setDuration(15000);
        setPosition(Position.TOP_END);
        addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
