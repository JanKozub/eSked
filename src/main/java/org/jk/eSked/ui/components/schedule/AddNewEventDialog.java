package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.model.types.EventType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import java.time.LocalDate;
import java.util.*;

public class AddNewEventDialog extends Dialog {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private final EventService eventService;
    private final LocalDate startOfWeek;
    private final UUID userId;
    private final Collection<ScheduleEntry> entries;
    private Runnable action;

    AddNewEventDialog(ScheduleService scheduleService, EventService eventService, LocalDate startOfWeek, ScheduleEntry scheduleEntry, UUID userId) {
        this.eventService = eventService;
        this.startOfWeek = startOfWeek;
        this.userId = userId;

        LocalDate eventDate = startOfWeek.plusDays(scheduleEntry.getDay());

        Label title = new Label("Nowe Wydarzenie");

        TextField topicField = new TextField();
        topicField.setPlaceholder("Temat");
        topicField.setWidth("100%");
        topicField.setErrorMessage(getTranslation(locale, "exception_empty_field"));

        ComboBox<EventType> eventType = new ComboBox<>();
        eventType.setPlaceholder("Rodzaj");
        eventType.setAllowCustomValue(false);
        eventType.setItems(EventType.values());
        eventType.setRenderer(new TextRenderer<>(EventType::getDescription));
        eventType.setItemLabelGenerator(EventType::getDescription);
        eventType.setWidth("100%");
        eventType.setErrorMessage(getTranslation(locale, "exception_empty_field"));

        Button confirm = new Button("Dodaj", e -> {
            if (!topicField.isEmpty()) {
                topicField.setInvalid(false);
                if (eventType.getValue() != null) {
                    eventType.setInvalid(false);
                    long time = TimeService.localDateToInstant(eventDate);
                    Event event = new Event(userId, eventService.createEventId(), eventType.getValue(),
                            topicField.getValue(), scheduleEntry.getHour(), true, time, TimeService.now());
                    eventService.addEvent(event);
                    new SuccessNotification("Dodano wydarzenie!", NotificationType.SHORT).open();
                    topicField.clear();
                    close();
                } else eventType.setInvalid(true);
            } else topicField.setInvalid(true);
        });
        confirm.setWidth("100%");

        VerticalLayout layout = new VerticalLayout(title, topicField, eventType, confirm);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setWidth("100%");

        Grid<Event> eventGrid = new Grid<>();
        updateEvents(eventGrid, eventDate);

        entries = scheduleService.getScheduleEntries(userId);
        eventGrid.addColumn(new BasicRenderer<>(event -> {
            if (entries != null) {
                for (ScheduleEntry entry : entries) {
                    if (entry.getHour() == event.getHour()
                            && entry.getDay() == TimeService.InstantToLocalDate(event.getTimestamp()).getDayOfWeek().getValue() - 1)
                        return entry.getSubject();
                }
            }
            return "brak";
        }) {
        }).setHeader("Lekcja");
        eventGrid.addColumn(event -> event.getType().getDescription()).setHeader("Rodzaj");
        eventGrid.addColumn(Event::getTopic).setHeader("Temat");
        eventGrid.addColumn(new ComponentRenderer<>(e -> {
            Icon icon = new Icon(VaadinIcon.TRASH);
            icon.getStyle().set("cursor", "pointer");
            icon.addClickListener(event -> {
                eventService.deleteEvent(userId, e.getEventId());
                updateEvents(eventGrid, eventDate);
            });
            return icon;
        })).setHeader("Usuń");
        eventGrid.setSelectionMode(Grid.SelectionMode.NONE);
        eventGrid.setSizeFull();
        eventGrid.setVerticalScrollingEnabled(true);
        eventGrid.setHeightByRows(true);

        if (!SessionService.isSessionMobile()) {
            setWidth("600px");
        }

        add(layout, eventGrid);
    }

    private void updateEvents(Grid<Event> eventGrid, LocalDate eventDate) {
        List<Event> eventsSorted = new ArrayList<>(eventService.getEventsForWeek(startOfWeek, userId));
        eventsSorted.removeIf(event -> TimeService.InstantToLocalDate(event.getTimestamp()).getDayOfWeek() != eventDate.getDayOfWeek());

        eventGrid.setItems(eventsSorted);
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
