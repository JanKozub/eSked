package org.jk.esked.app.ui.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.ui.components.MyDatePicker;
import org.jk.esked.app.ui.components.SuccessNotification;
import org.jk.esked.app.ui.components.TopicField;
import org.jk.esked.app.ui.components.events.EventGrid;
import org.jk.esked.app.ui.components.events.EventTypeComboBox;
import org.jk.esked.app.backend.model.Event;
import org.jk.esked.app.backend.model.User;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.EventService;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.TimeService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

@PermitAll
@Route(value = "events/new", layout = MainLayout.class) //todo check code
public class NewEventView extends HorizontalLayout implements HasDynamicTitle {
    private final EventService eventService;
    private final EventGrid eventGrid;
    private final DatePicker datePicker;
    private final EventTypeComboBox eventType;
    private final NumberField hourNum;
    private final TopicField topicField;
    private final User user;

    public NewEventView(ScheduleService scheduleService, EventService eventService, SecurityService securityService) {
        this.eventService = eventService;
        this.user = securityService.getUser();
        this.eventGrid = new EventGrid(user.getId(), scheduleService, eventService, LocalDate.now());

        Text formLabel = new Text(getTranslation("new.event.title"));
//        formLabel.getStyle().set("margin-left", "auto");
//        formLabel.getStyle().set("margin-right", "auto");
        HorizontalLayout formName = new HorizontalLayout(formLabel);
        formName.setSizeFull();

        datePicker = new MyDatePicker();
        datePicker.addValueChangeListener(e -> {
            try {
                validateDate(datePicker.getValue());
                datePicker.setInvalid(false);
            } catch (ValidationException ex) {
                datePicker.setErrorMessage(ex.getMessage());
                datePicker.setInvalid(true);
            }
        });

        eventType = new EventTypeComboBox();
        topicField = new TopicField();

        hourNum = new NumberField();
        hourNum.setStepButtonsVisible(true);
        hourNum.setMin(1);
        hourNum.setPlaceholder(getTranslation("hour"));
        hourNum.setWidth("100%");

        Button addButton = new Button(getTranslation("add"), onClick -> addEvent());
        addButton.setWidth("100%");

        FormLayout eventForm = new FormLayout();

        eventForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("15em", 1),
                new FormLayout.ResponsiveStep("15em", 2));
        eventForm.add(formName, datePicker, eventType, hourNum, topicField, addButton);
        eventForm.setColspan(formName, 2);
        eventForm.setColspan(datePicker, 2);
        eventForm.setColspan(eventType, 1);
        eventForm.setColspan(hourNum, 1);
        eventForm.setColspan(topicField, 2);
        eventForm.setColspan(addButton, 2);
        eventForm.setSizeFull();

        VerticalLayout newEventLayout = new VerticalLayout(eventForm);

        Text gridLabel = new Text(getTranslation("new.event.event.list.label"));
//        gridLabel.getStyle().set("margin-left", "auto");
//        gridLabel.getStyle().set("margin-right", "auto");

        VerticalLayout gridLayout = new VerticalLayout(gridLabel, eventGrid);

        newEventLayout.setWidth("50%");
        gridLayout.setWidth("50%");
        add(newEventLayout, gridLayout);
    }

    private void validateDate(LocalDate date) throws ValidationException {
        if (date == null) throw new ValidationException(getTranslation("exception.empty.field"));

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
            throw new ValidationException(getTranslation("exception.sat.sun.not.exist"));

        if (date.isBefore(LocalDate.now()))
            throw new ValidationException(getTranslation("exception.event.in.past"));

        eventGrid.reloadForDay(date);
    }

    private void addEvent() {
        try {
            validateEvent();

            long time = TimeService.localDateToInstant(datePicker.getValue());
            Event event = new Event();
            event.setUser(user);
            event.setEventType(eventType.getValue());
            event.setTopic(topicField.getValue());
            event.setHour((int) Math.round(hourNum.getValue()));
            event.setCheckedFlag(true);
            event.setTimestamp(time);
            event.setCreatedTimestamp(TimeService.now());

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
