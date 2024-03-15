package org.jk.eSked.ui.components.myComponents;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
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
        HorizontalLayout layout = new HorizontalLayout(new Text(text), new Button("Ok", buttonClickEvent -> close()));
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(layout);
    }
}
