package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.types.EventType;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.EventService;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.backend.services.utilities.TimeService;
import org.jk.esked.app.frontend.components.buttons.WideButton;
import org.jk.esked.app.frontend.components.events.EventGrid;
import org.jk.esked.app.frontend.components.events.EventTypeComboBox;
import org.jk.esked.app.frontend.components.fields.TopicField;
import org.jk.esked.app.frontend.components.notifications.SuccessNotification;

import java.time.LocalDate;

public class AddNewEventDialog extends Dialog {
    private Runnable action;

    public AddNewEventDialog(User user, ScheduleEntryService scheduleEntryService, EventService eventService, LocalDate startOfWeek, ScheduleEntry scheduleEntry) {
        LocalDate eventDate = startOfWeek.plusDays(scheduleEntry.getDay());

        Text title = new Text(getTranslation("new.event.title"));
        TopicField topicField = new TopicField();
        ComboBox<EventType> eventType = new EventTypeComboBox();

        Button confirm = new WideButton(getTranslation("add"), e -> {
            if (topicField.isEmpty()) {
                topicField.setInvalid(true);
                return;
            }

            if (eventType.getValue() == null) {
                eventType.setInvalid(true);
                return;
            }

            eventType.setInvalid(false);
            eventService.saveEvent(user, eventType.getValue(), topicField.getValue(), scheduleEntry.getHour(), true, TimeService.localDateToTimestamp(eventDate));

            new SuccessNotification(getTranslation("new.event.added") + "!", NotificationType.SHORT).open();
            topicField.clear();
            close();
        });

        EventGrid eventGrid = new EventGrid(user.getId(), scheduleEntryService, eventService, LocalDate.now());
        eventGrid.reloadForDay(eventDate);

        addClassName("new-event-dialog");
        add(new VerticalLayout(title, topicField, eventType, confirm), eventGrid);
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
