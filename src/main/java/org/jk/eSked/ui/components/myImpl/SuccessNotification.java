package org.jk.eSked.ui.components.myImpl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class SuccessNotification extends Notification {

    public SuccessNotification(String text) {
        super(text);
        setDuration(5000);
        setPosition(Position.TOP_END);
        addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
