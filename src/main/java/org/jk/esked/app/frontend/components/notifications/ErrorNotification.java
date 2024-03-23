package org.jk.esked.app.frontend.components.notifications;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class ErrorNotification extends Notification {
    public ErrorNotification(String text, int duration) {
        super(text, duration);
        addThemeVariants(NotificationVariant.LUMO_ERROR);
        setPosition(Notification.Position.TOP_END);
    }
}
