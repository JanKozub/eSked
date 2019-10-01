package org.jk.eSked.ui.components.myImpl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.jk.eSked.backend.model.types.NotificationType;

public class SuccessNotification extends Notification {

    public SuccessNotification(String text, NotificationType type) {
        super(text);
        setDuration(type.getDuration());
        setPosition(Position.TOP_END);
        addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
