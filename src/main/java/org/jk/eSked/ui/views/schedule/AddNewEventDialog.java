package org.jk.eSked.ui.views.schedule;

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
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.model.schedule.ScheduleEvent;
import org.jk.eSked.backend.model.types.EventType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EventService;
import org.jk.eSked.backend.service.ScheduleService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.ui.components.myImpl.SuccessNotification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AddNewEventDialog extends Dialog {
    private final EventService eventService;
    private final LocalDate startOfWeek;
    private final UUID userId;
    private final Collection<ScheduleEntry> entries;
    private Runnable action;

    public AddNewEventDialog(ScheduleService scheduleService, EventService eventService, LocalDate startOfWeek, ScheduleEntry scheduleEntry, UUID userId) {
        this.eventService = eventService;
        this.startOfWeek = startOfWeek;
        this.userId = userId;

        LocalDate eventDate = startOfWeek.plusDays(scheduleEntry.getDay());

        Label title = new Label("Nowe Wydarzenie");

        TextField topicField = new TextField();
        topicField.setPlaceholder("Temat");
        topicField.setWidth("100%");
        topicField.setErrorMessage("Pole nie może być puste");

        ComboBox<EventType> eventType = new ComboBox<>();
        eventType.setPlaceholder("Rodzaj");
        eventType.setAllowCustomValue(false);
        eventType.setItems(EventType.values());
        eventType.setRenderer(new TextRenderer<>(EventType::getDescription));
        eventType.setItemLabelGenerator(EventType::getDescription);
        eventType.setWidth("100%");
        eventType.setErrorMessage("Pole nie może być puste");

        Button confirm = new Button("Dodaj", e -> {
            if (!topicField.isEmpty()) {
                topicField.setInvalid(false);
                if (eventType.getValue() != null) {
                    eventType.setInvalid(false);
                    long time = eventDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
                    UUID id = UUID.randomUUID();
                    ScheduleEvent event = new ScheduleEvent(userId, id, time, scheduleEntry.getHour(),
                            eventType.getValue(), topicField.getValue(), Instant.now().toEpochMilli());
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
                    if (entry.getHour() == event.getHour() && entry.getDay() == event.getDate().getDayOfWeek().getValue() - 1)
                        return entry.getSubject();
                }
            }
            return "brak";
        }) {
        }).setHeader("Lekcja");
        eventGrid.addColumn(event -> event.getEventType().getDescription()).setHeader("Rodzaj");
        eventGrid.addColumn(Event::getTopic).setHeader("Temat");
        eventGrid.addColumn(new ComponentRenderer<>(e -> {
            Icon icon = new Icon(VaadinIcon.TRASH);
            icon.getStyle().set("cursor", "pointer");
            icon.addClickListener(event -> {
                eventService.deleteEvent(new ScheduleEvent(userId, e.getId(), e.getDate().toInstant(ZoneOffset.UTC).toEpochMilli(),
                        e.getHour(), e.getEventType(), e.getTopic(), e.getCreatedDate().toInstant(ZoneOffset.UTC).toEpochMilli()));
                updateEvents(eventGrid, eventDate);
            });
            return icon;
        })).setHeader("Usuń");
        eventGrid.setSelectionMode(Grid.SelectionMode.NONE);
        eventGrid.setSizeFull();
        eventGrid.setVerticalScrollingEnabled(true);
        eventGrid.setHeightByRows(true);

        if (!SessionService.isSessionMoblie()) {
            setWidth("600px");
        }

        add(layout, eventGrid);
    }

    private void updateEvents(Grid<Event> eventGrid, LocalDate eventDate) {
        List<Event> eventsSorted = new ArrayList<>(eventService.getEvents(startOfWeek, userId));
        eventsSorted.removeIf(event -> event.getDate().getDayOfWeek() != eventDate.getDayOfWeek());

        eventGrid.setItems(eventsSorted);
    }

    public void setRefreshAction(Runnable action) {
        this.action = action;
    }

    @Override
    public void close() {
        if (this.action != null)
            this.action.run();

        super.close();
    }
}