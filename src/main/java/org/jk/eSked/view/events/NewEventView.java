package org.jk.eSked.view.events;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.component.SimplePopup;
import org.jk.eSked.model.User;
import org.jk.eSked.model.entry.ScheduleEntry;
import org.jk.eSked.model.event.Event;
import org.jk.eSked.model.event.EventType;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.menu.MenuView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Route(value = "events/new", layout = MenuView.class)
@PageTitle("Nowe Wydarzenie")
public class NewEventView extends HorizontalLayout {

    public NewEventView(LoginService loginService, ScheduleService scheduleService, EventService eventService) {
        SimplePopup simplePopup = new SimplePopup();

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
                        simplePopup.open("Sobota ani niedziela nie istnieją na planie");
                        datePicker.setValue(null);
                    } else {
                        if (datePicker.getValue().isBefore(LocalDate.now())) {
                            datePicker.clear();
                            simplePopup.open("Nie możesz ustawić wydarzenia w przeszłości");
                        } else {
                            Collection<Event> events = eventService.getEvents(datePicker.getValue(), userId);
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

            ComboBox<EventType> eventType = new ComboBox<>();
            eventType.setAllowCustomValue(false);
            eventType.setItems(EventType.values());
            eventType.setRenderer(new TextRenderer<>(EventType::getDescription));
            eventType.setPlaceholder("Rodzaj");
            eventType.setItemLabelGenerator(EventType::getDescription);
            eventType.setWidth("50%");

            ComboBox<Integer> hourBox = new ComboBox<>();
            hourBox.setAllowCustomValue(true);
            int maxHour = 0;
            for (ScheduleEntry entry : entries) {
                if (entry.getHour() > maxHour) maxHour = entry.getHour();
            }
            List<Integer> hours = new ArrayList<>();
            for (int i = 0; i <= maxHour; i++) hours.add(i + 1);
            hourBox.setItems(hours);
            hourBox.setPlaceholder("Godzina");
            hourBox.setWidth("50%");

            HorizontalLayout horizontalContainer = new HorizontalLayout();
            horizontalContainer.setWidth("100%");
            horizontalContainer.add(eventType, hourBox);

            TextField textField = new TextField();
            textField.setWidth("100%");
            textField.setPlaceholder("Temat");

            Button addButton = new Button("Dodaj!", e -> {
                EventDialogMethods eventDialogMethods = new EventDialogMethods();
                if (hourBox.getValue() != null) {
                    if (eventDialogMethods.onAdd(eventService, datePicker.getValue(),
                            hourBox.getValue() - 1, eventType.getValue(), textField.getValue(), userId)) {
                        datePicker.clear();
                        eventType.clear();
                        hourBox.clear();
                        textField.clear();
                    }
                } else simplePopup.open("Pole z godziną nie może być puste");
            });
            addButton.setWidth("100%");

            VerticalLayout leftPanel = new VerticalLayout(pageInfo, datePicker, horizontalContainer, textField, addButton);
            leftPanel.setWidth("50%");

            add(leftPanel, rightPanel);
        }
    }
}
