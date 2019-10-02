package org.jk.eSked.ui.components.myImpl;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.jk.eSked.backend.model.types.NotificationType;

public class SuccessNotification extends Notification {

    public SuccessNotification(String text, NotificationType type) {
        setDuration(type.getDuration());
        setPosition(Position.TOP_END);
        addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        HorizontalLayout layout = new HorizontalLayout(new Label(text), new Button("Ok", buttonClickEvent -> close()));
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(layout);
    }
}
