package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.esked.app.backend.model.entities.Event;
import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.types.EventType;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.EventService;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.utilities.TimeService;
import org.jk.esked.app.frontend.components.other.SuccessNotification;
import org.jk.esked.app.frontend.components.fields.TopicField;
import org.jk.esked.app.frontend.components.events.EventGrid;
import org.jk.esked.app.frontend.components.events.EventTypeComboBox;

import java.time.LocalDate;

public class AddNewEventDialog extends Dialog {
    private Runnable action;

    public AddNewEventDialog(User user, ScheduleService scheduleService, EventService eventService, LocalDate startOfWeek, ScheduleEntry scheduleEntry) {
        LocalDate eventDate = startOfWeek.plusDays(scheduleEntry.getDay());

        Text title = new Text(getTranslation("new.event.title"));
        TopicField topicField = new TopicField();
        ComboBox<EventType> eventType = new EventTypeComboBox();

        Button confirm = new Button(getTranslation("add"), e -> {
            if (!topicField.isEmpty()) {
                topicField.setInvalid(false);
                if (eventType.getValue() != null) {
                    eventType.setInvalid(false);
                    long time = TimeService.localDateToTimestamp(eventDate);
                    Event event = new Event();
                    event.setUser(user);
                    event.setEventType(eventType.getValue());
                    event.setTopic(topicField.getValue());
                    event.setHour(scheduleEntry.getHour());
                    event.setCheckedFlag(true);
                    event.setTimestamp(time);
                    event.setCreatedTimestamp(TimeService.now());
                    eventService.saveEvent(event);

                    new SuccessNotification(getTranslation("new.event.added") + "!", NotificationType.SHORT).open();
                    topicField.clear();
                    close();
                } else eventType.setInvalid(true);
            } else topicField.setInvalid(true);
        });
        confirm.setWidth("100%");

        VerticalLayout layout = new VerticalLayout(title, topicField, eventType, confirm);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setWidth("100%");

        EventGrid eventGrid = new EventGrid(user.getId(), scheduleService, eventService, startOfWeek);

        add(layout, eventGrid);
    }

    void setRefreshAction(Runnable action) {
        this.action = action;
    }

    @Override
    public void close() {
        if (this.action != null)
            this.action.run();

        super.close();
    }
}
