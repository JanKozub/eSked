package org.jk.esked.app.frontend.views.events;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.backend.model.entities.Event;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.EventService;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.backend.services.utilities.TimeService;
import org.jk.esked.app.frontend.components.events.EventGrid;
import org.jk.esked.app.frontend.components.events.EventTypeComboBox;
import org.jk.esked.app.frontend.components.fields.TopicField;
import org.jk.esked.app.frontend.components.other.MyDatePicker;
import org.jk.esked.app.frontend.components.other.SuccessNotification;
import org.jk.esked.app.frontend.views.MainLayout;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

@PermitAll
@CssImport("./styles/new-event.css")
@Route(value = "events/new", layout = MainLayout.class)
public class NewEventView extends HorizontalLayout implements HasDynamicTitle {
    private final EventGrid eventGrid;
    private final DatePicker datePicker = new MyDatePicker();
    private final EventTypeComboBox eventType = new EventTypeComboBox();
    private final NumberField hourNum = new NumberField();
    private final TopicField topicField = new TopicField();

    public NewEventView(ScheduleEntryService scheduleEntryService, EventService eventService, SecurityService securityService) {
        User user = securityService.getUser();
        this.eventGrid = new EventGrid(user.getId(), scheduleEntryService, eventService, LocalDate.now());

        Span formLabel = new Span(getTranslation("new.event.title"));

        datePicker.addValueChangeListener(e -> validateDate(datePicker.getValue()));

        hourNum.setStepButtonsVisible(true);
        hourNum.setMin(1);
        hourNum.setPlaceholder(getTranslation("hour"));

        Button addButton = new Button(getTranslation("add"), onClick -> addEvent(user, eventService));

        FormLayout eventForm = new FormLayout();
        eventForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("15em", 1),
                new FormLayout.ResponsiveStep("15em", 2));
        eventForm.add(formLabel, datePicker, eventType, hourNum, topicField, addButton);
        eventForm.setColspan(formLabel, 2);
        eventForm.setColspan(datePicker, 2);
        eventForm.setColspan(eventType, 1);
        eventForm.setColspan(hourNum, 1);
        eventForm.setColspan(topicField, 2);
        eventForm.setColspan(addButton, 2);

        Span gridLabel = new Span(getTranslation("new.event.event.list.label"));

        add(new VerticalLayout(eventForm), new VerticalLayout(gridLabel, eventGrid));
    }

    private void validateDate(LocalDate date) {
        try {
            if (date == null) throw new ValidationException(getTranslation("exception.empty.field"));

            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                throw new ValidationException(getTranslation("exception.sat.sun.not.exist"));

            if (date.isBefore(LocalDate.now()))
                throw new ValidationException(getTranslation("exception.event.in.past"));

            datePicker.setInvalid(false);
        } catch (ValidationException exception) {
            datePicker.setErrorMessage(exception.getMessage());
            datePicker.setInvalid(true);
        }
        eventGrid.reloadForDay(date);
    }

    private void addEvent(User user, EventService eventService) {
        try {
            validateEvent();

            Event event = new Event();
            event.setUser(user);
            event.setEventType(eventType.getValue());
            event.setTopic(topicField.getValue());
            event.setHour((int) Math.round(hourNum.getValue()));
            event.setCheckedFlag(true);
            event.setTimestamp(TimeService.localDateToTimestamp(datePicker.getValue()));

            eventService.saveEvent(event);
            new SuccessNotification(getTranslation("new.event.added") + ": " + topicField.getValue(), NotificationType.SHORT).open();

            datePicker.clear();
            datePicker.setInvalid(false);
            eventType.clear();
            hourNum.clear();
            topicField.clear();

            eventGrid.setItems(new ArrayList<>());
        } catch (ValidationException ex) {
            Notification notification = new Notification(ex.getMessage(), 5000, Notification.Position.TOP_END);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        }
    }

    private void validateEvent() throws ValidationException {
        if (datePicker.isEmpty()) throw new ValidationException(getTranslation("exception.date.field.empty"));

        if (eventType.isEmpty()) throw new ValidationException(getTranslation("exception.type.field.empty"));

        if (hourNum.isEmpty()) throw new ValidationException(getTranslation("exception.hour.field.empty"));

        if (topicField.isEmpty()) throw new ValidationException(getTranslation("exception.topic.field.empty"));
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.events.new");
    }
}
