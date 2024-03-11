package org.jk.eSked.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.ui.components.events.EventGrid;
import org.jk.eSked.ui.components.events.EventTypeComboBox;
import org.jk.eSked.ui.components.myComponents.MyDatePicker;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.jk.eSked.ui.components.myComponents.TopicField;

import javax.validation.ValidationException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

@Route(value = "events/new", layout = MainLayout.class)
@PageTitle("Nowe Wydarzenie")
public class NewEventView extends HorizontalLayout {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private final EventService eventService;
    private final EventGrid eventGrid;
    private final DatePicker datePicker;
    private final EventTypeComboBox eventType;
    private final NumberField hourNum;
    private final TopicField topicField;

    public NewEventView(ScheduleService scheduleService, EventService eventService) {
        this.eventService = eventService;
        this.eventGrid = new EventGrid(scheduleService, eventService, LocalDate.now());
        SessionService.setAutoTheme();

        Label formLabel = new Label(getTranslation(locale, "new_event_title"));
        formLabel.getStyle().set("margin-left", "auto");
        formLabel.getStyle().set("margin-right", "auto");
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
        hourNum.setHasControls(true);
        hourNum.setMin(1);
        hourNum.setPlaceholder(getTranslation(locale, "hour"));
        hourNum.setWidth("100%");

        Button addButton = new Button(getTranslation(locale, "add"), onClick -> addEvent());
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

        Label gridLabel = new Label(getTranslation(locale, "new_event_event_list_label"));
        gridLabel.getStyle().set("margin-left", "auto");
        gridLabel.getStyle().set("margin-right", "auto");

        VerticalLayout gridLayout = new VerticalLayout(gridLabel, eventGrid);

        if (SessionService.isSessionMobile())
            add(new VerticalLayout(newEventLayout, gridLayout));
        else {
            newEventLayout.setWidth("50%");
            gridLayout.setWidth("50%");
            add(newEventLayout, gridLayout);
        }
    }

    private void validateDate(LocalDate date) throws ValidationException {
        if (date == null) throw new ValidationException(getTranslation(locale, "exception_empty_field"));

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
            throw new ValidationException(getTranslation(locale, "exception_sat_sun_not_exist"));

        if (date.isBefore(LocalDate.now()))
            throw new ValidationException(getTranslation(locale, "exception_event_in_past"));

        eventGrid.reloadForDay(date);
    }

    private void addEvent() {
        try {
            validateEvent();

            long time = TimeService.localDateToInstant(datePicker.getValue());
            Event event = new Event(SessionService.getUserId(), eventService.createEventId(), eventType.getValue(), topicField.getValue(),
                    (int) Math.round(hourNum.getValue()), true, time, TimeService.now());
            eventService.addEvent(event);
            new SuccessNotification(getTranslation(locale, "new_event_added") + ": " + topicField.getValue(), NotificationType.SHORT).open();

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
        if (datePicker.isEmpty()) throw new ValidationException(getTranslation(locale, "exception_date_field_empty"));

        if (eventType.isEmpty()) throw new ValidationException(getTranslation(locale, "exception_type_field_empty"));

        if (hourNum.isEmpty()) throw new ValidationException(getTranslation(locale, "exception_hour_field_empty"));

        if (topicField.isEmpty()) throw new ValidationException(getTranslation(locale, "exception_topic_field_empty"));
    }
}
