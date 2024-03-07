package org.jk.eSked.ui.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.model.types.EventType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.ui.views.MainLayout;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import com.vaadin.flow.component.button.Button;

import javax.validation.ValidationException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Route(value = "events/new", layout = MainLayout.class)
@PageTitle("Nowe Wydarzenie")
public class NewEventView extends HorizontalLayout {
    private final EventService eventService;
    private final Grid<Event> eventGrid;
    private final UUID userId;
    private final DatePicker datePicker;
    private final ComboBox<EventType> eventType;
    private final NumberField hourNum;
    private final TextField topicField;

    public NewEventView(ScheduleService scheduleService, EventService eventService) {
        this.eventService = eventService;
        this.eventGrid = new Grid<>();
        this.userId = SessionService.getUserId();
        SessionService.setAutoTheme();

        Label formLabel = new Label("Nowe Wydarzenie");
        formLabel.getStyle().set("margin-left", "auto");
        formLabel.getStyle().set("margin-right", "auto");
        HorizontalLayout formName = new HorizontalLayout(formLabel);
        formName.setSizeFull();

        datePicker = new DatePicker();
        datePicker.setI18n(polishI18nDatePicker());
        datePicker.setWidth("100%");
        datePicker.addValueChangeListener(e -> {
            try {
                validateDate(datePicker.getValue());
                datePicker.setInvalid(false);
            } catch (ValidationException ex) {
                datePicker.setErrorMessage(ex.getMessage());
                datePicker.setInvalid(true);
            }
        });

        eventType = new ComboBox<>();
        eventType.setItems(EventType.values());
        eventType.setRenderer(new TextRenderer<>(EventType::getDescription));
        eventType.setPlaceholder("Rodzaj");
        eventType.setItemLabelGenerator(EventType::getDescription);
        eventType.setWidth("100%");

        hourNum = new NumberField();
        hourNum.setHasControls(true);
        hourNum.setMin(1);
        hourNum.setPlaceholder("Godzina");
        hourNum.setWidth("100%");

        topicField = new TextField();
        topicField.setPlaceholder("Temat");
        topicField.setWidth("100%");

        Button addButton = new Button("Dodaj!", onClick -> addEvent());
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

        Label gridLabel = new Label("Wydarzenia w tym dniu");
        gridLabel.getStyle().set("margin-left", "auto");
        gridLabel.getStyle().set("margin-right", "auto");

        eventGrid.setHeightByRows(true);
        eventGrid.addColumn(event -> event.getType().getDescription()).setHeader("Rodzaj");
        eventGrid.addColumn(new BasicRenderer<>(event -> {
            Collection<ScheduleEntry> entries = scheduleService.getScheduleEntries(userId);
            if (!entries.isEmpty()) {
                for (ScheduleEntry entry : entries) {
                    if (entry.getHour() == event.getHour() && entry.getDay() == TimeService.InstantToLocalDate(event.getTimestamp()).getDayOfWeek().getValue() - 1)
                        return entry.getSubject();
                }
            }
            return "brak";
        }) {
        }).setHeader("Lekcja");
        eventGrid.addColumn(Event::getHour).setHeader("Godzina");
        eventGrid.addColumn(Event::getTopic).setHeader("Temat");
        eventGrid.getStyle().set("margin-top", "0px");

        VerticalLayout gridLayout = new VerticalLayout(gridLabel, eventGrid);

        if (SessionService.isSessionMobile())
            add(new VerticalLayout(newEventLayout, gridLayout));
        else {
            newEventLayout.setWidth("50%");
            gridLayout.setWidth("50%");
            add(newEventLayout, gridLayout);
        }
    }

    private DatePicker.DatePickerI18n polishI18nDatePicker() {
        return new DatePicker.DatePickerI18n()
                .setWeek("tydzień")
                .setCalendar("kalendarz")
                .setClear("wyczyść")
                .setToday("dzisiaj")
                .setCancel("zamknij")
                .setFirstDayOfWeek(1)
                .setMonthNames(List.of("styczeń", "luty", "marzec", "kwiecień", "maj", "czerwiec", "lipiec", "sierpień", "wrzesień", "październik", "listopad", "grudzień"))
                .setWeekdays(List.of("niedziela", "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota"))
                .setWeekdaysShort(List.of("niedź", "pon", "wt", "śr", "czw", "pt", "sob"));
    }

    private void validateDate(LocalDate date) throws ValidationException {
        if (date == null) throw new ValidationException("Pole nie może być puste");

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
            throw new ValidationException("Sobota ani niedziela nie istnieją na planie");

        if (date.isBefore(LocalDate.now()))
            throw new ValidationException("Nie możesz ustawić wydarzenia w przeszłości");

        List<Event> eventsOnDay = new ArrayList<>();
        Collection<Event> events = eventService.getEventsForWeek(date, userId);
        for (Event event : events) {
            if (TimeService.InstantToLocalDate(event.getTimestamp()).equals(date))
                eventsOnDay.add(event);
        }
        eventGrid.setItems(eventsOnDay);
    }

    private void addEvent() {
        try {
            validateEvent();

            long time = TimeService.localDateToInstant(datePicker.getValue());
            Event event = new Event(userId, eventService.createEventId(), eventType.getValue(), topicField.getValue(),
                    (int) Math.round(hourNum.getValue()), true, time, TimeService.now());
            eventService.addEvent(event);
            new SuccessNotification("Dodano wydarzenie: " + topicField.getValue(), NotificationType.SHORT).open();

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
        if (datePicker.isEmpty()) throw new ValidationException("Pole z datą nie może być puste");

        if (eventType.isEmpty()) throw new ValidationException("Pole z typem wydarzenia nie może być puste");

        if (hourNum.isEmpty()) throw new ValidationException("Pole z godziną wydarzenia nie może być puste");

        if (topicField.isEmpty()) throw new ValidationException("Pole z tematem wydarzenia nie może być puste");
    }
}
