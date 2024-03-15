package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.model.types.EventType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.ui.components.events.EventGrid;
import org.jk.eSked.ui.components.events.EventTypeComboBox;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.jk.eSked.ui.components.myComponents.TopicField;

import java.time.LocalDate;
import java.util.UUID;

public class AddNewEventDialog extends Dialog {
    private Runnable action;

    AddNewEventDialog(ScheduleService scheduleService, EventService eventService, LocalDate startOfWeek, ScheduleEntry scheduleEntry, UUID userId) {
        LocalDate eventDate = startOfWeek.plusDays(scheduleEntry.getDay());

        Text title = new Text(getTranslation("new.event.title"));
        TopicField topicField = new TopicField();
        ComboBox<EventType> eventType = new EventTypeComboBox();

        Button confirm = new Button(getTranslation("add"), e -> {
            if (!topicField.isEmpty()) {
                topicField.setInvalid(false);
                if (eventType.getValue() != null) {
                    eventType.setInvalid(false);
                    long time = TimeService.localDateToInstant(eventDate);
                    Event event = new Event(userId, eventService.createEventId(), eventType.getValue(),
                            topicField.getValue(), scheduleEntry.getHour(), true, time, TimeService.now());
                    eventService.addEvent(event);
                    new SuccessNotification(getTranslation("new.event.added") +"!", NotificationType.SHORT).open();
                    topicField.clear();
                    close();
                } else eventType.setInvalid(true);
            } else topicField.setInvalid(true);
        });
        confirm.setWidth("100%");

        VerticalLayout layout = new VerticalLayout(title, topicField, eventType, confirm);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setWidth("100%");

        EventGrid eventGrid = new EventGrid(scheduleService, eventService, startOfWeek);

        if (!SessionService.isSessionMobile()) setWidth("600px");

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
