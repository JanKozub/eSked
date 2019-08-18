package org.jk.eSked.view.events;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.model.User;
import org.jk.eSked.model.entry.ScheduleEntry;
import org.jk.eSked.model.event.Event;
import org.jk.eSked.model.event.EventType;
import org.jk.eSked.model.event.ScheduleEvent;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.view.menu.MenuView;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Route(value = "events/new", layout = MenuView.class)
@PageTitle("Nowe Wydarzenie")
public class NewEventView extends HorizontalLayout {

    private Collection<Event> events;

    public NewEventView(LoginService loginService, ScheduleService scheduleService, EventService eventService) {
        if (loginService.checkIfUserIsLogged()) {
            UUID userId = VaadinSession.getCurrent().getAttribute(User.class).getId();

            Collection<ScheduleEntry> entries = scheduleService.getScheduleEntries(userId);

            Label gridName = new Label("Wydarzenia w tym dniu");

            Grid<Event> eventGrid = new Grid<>();
            eventGrid.setHeightByRows(true);
            eventGrid.addColumn(event -> event.getEventType().getDescription()).setHeader("Rodzaj");
            eventGrid.addColumn(new BasicRenderer<>(event -> {
                if (entries != null) {
                    for (ScheduleEntry entry : entries) {
                        if (entry.getHour() == event.getHour() && entry.getDay() == event.getDate().getDayOfWeek().getValue() - 1)
                            return entry.getSubject();
                    }
                }
                return "brak";
            }) {
            }).setHeader("Lekcja");
            eventGrid.addColumn(Event::getHour).setHeader("Godzina");
            eventGrid.addColumn(Event::getTopic).setHeader("Temat");

            VerticalLayout rightPanel = new VerticalLayout(gridName, eventGrid);
            rightPanel.setWidth("50%");

            Label pageInfo = new Label("Nowe wydarzenie");

            DatePicker datePicker = new DatePicker();
            datePicker.setI18n(new DatePicker.DatePickerI18n().setWeek("tydzień").setCalendar("kalendarz")
                    .setClear("wyczyść").setToday("dzisiaj")
                    .setCancel("zamknij").setFirstDayOfWeek(1)
                    .setMonthNames(Arrays.asList("styczeń", "luty", "marzec", "kwiecień", "maj", "czerwiec", "lipiec", "sierpień", "wrzesień", "październik", "listopad", "grudzień"))
                    .setWeekdays(Arrays.asList("niedziela", "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota"))
                    .setWeekdaysShort(Arrays.asList("niedź", "pon", "wt", "śr", "czw", "pt", "sob")));
            datePicker.setWidth("100%");
            datePicker.addValueChangeListener(e -> {
                List<Event> eventsOnDay = new ArrayList<>();
                if (datePicker.getValue() != null) {
                    if (datePicker.getValue().getDayOfWeek() == DayOfWeek.SATURDAY || datePicker.getValue().getDayOfWeek() == DayOfWeek.SUNDAY) {
                        datePicker.setErrorMessage("Sobota ani niedziela nie istnieją na planie");
                        datePicker.setInvalid(true);
                        datePicker.setValue(null);
                    } else {
                        if (datePicker.getValue().isBefore(LocalDate.now())) {
                            datePicker.clear();
                            datePicker.setErrorMessage("Nie możesz ustawić wydarzenia w przeszłości");
                            datePicker.setInvalid(true);
                        } else {
                            datePicker.setInvalid(false);
                            datePicker.setErrorMessage("Data musi być wskazana");
                            events = eventService.getEvents(datePicker.getValue(), userId);
                            for (Event event : events) {
                                if (event.getDate().equals(datePicker.getValue())) {
                                    eventsOnDay.add(event);
                                }
                            }
                        }
                    }
                    eventGrid.setItems(eventsOnDay);
                }
            });
            datePicker.setErrorMessage("Data musi być wskazana");

            ComboBox<EventType> eventType = new ComboBox<>();
            eventType.setItems(EventType.values());
            eventType.setRenderer(new TextRenderer<>(EventType::getDescription));
            eventType.setPlaceholder("Rodzaj");
            eventType.setItemLabelGenerator(EventType::getDescription);
            eventType.setWidth("50%");
            eventType.setErrorMessage("Pole nie może być puste");

            NumberField hourBox = new NumberField();
            hourBox.setHasControls(true);
            hourBox.setMin(1);
            hourBox.setPlaceholder("Godzina");
            hourBox.setWidth("50%");
            hourBox.setErrorMessage("Pole nie może być puste");

            HorizontalLayout horizontalContainer = new HorizontalLayout();
            horizontalContainer.setWidth("100%");
            horizontalContainer.add(eventType, hourBox);

            TextField textField = new TextField();
            textField.setWidth("100%");
            textField.setPlaceholder("Temat");
            textField.setErrorMessage("Pole nie może być puste");

            Button addButton = new Button("Dodaj!", e -> {
                if (!datePicker.isEmpty()) {
                    datePicker.setInvalid(false);
                    if (eventType.getValue() != null) {
                        eventType.setInvalid(false);
                        if (!hourBox.isEmpty()) {
                            hourBox.setInvalid(false);
                            if (textField.getValue() != null && !textField.getValue().equals("")) {
                                textField.setInvalid(false);
                                long time = datePicker.getValue().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
                                UUID id = UUID.randomUUID();
                                ScheduleEvent event = new ScheduleEvent(userId, id, time, (int) Math.round(hourBox.getValue()),
                                        eventType.getValue(), textField.getValue(), Instant.now().toEpochMilli());
                                eventService.addEvent(event);
                                events = eventService.getEvents(datePicker.getValue(), userId);
                                datePicker.clear();
                                eventType.clear();
                                hourBox.clear();
                                Notification notification = new Notification("Dodano wydarzenie " + textField.getValue(), 5000, Notification.Position.TOP_END);
                                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                                textField.clear();
                                notification.open();
                            } else textField.setInvalid(true);
                        } else hourBox.setInvalid(true);
                    } else eventType.setInvalid(true);
                } else datePicker.setInvalid(true);
            });
            addButton.setWidth("100%");

            VerticalLayout leftPanel = new VerticalLayout(pageInfo, datePicker, horizontalContainer, textField, addButton);
            leftPanel.setWidth("50%");

            add(leftPanel, rightPanel);
        }
    }
}
