package org.jk.eSked.view.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.jk.eSked.model.entry.ScheduleEntry;
import org.jk.eSked.model.event.Event;
import org.jk.eSked.model.event.EventType;
import org.jk.eSked.model.event.ScheduleEvent;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.view.events.EventDialogMethods;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AddNewEventDialog extends Dialog {
    private final ScheduleService scheduleService;
    private final EventService eventService;
    private final LocalDate startOfWeek;
    private final UUID userID;
    private final LocalDate eventDate;
    private Collection<ScheduleEntry> entries;
    private final Grid<Event> eventGrid;
    private Runnable action;


    public AddNewEventDialog(ScheduleService scheduleService, EventService eventService, GroupsService groupsService, UserService userService, LocalDate startOfWeek, ScheduleEntry scheduleEntry, UUID userID) {
        this.scheduleService = scheduleService;
        this.eventService = eventService;
        this.eventDate = startOfWeek.plusDays(scheduleEntry.getDay());
        this.startOfWeek = startOfWeek;
        this.userID = userID;

        Icon dialogClose = new Icon(VaadinIcon.CLOSE);
        dialogClose.getStyle().set("margin-left", "auto");
        dialogClose.getStyle().set("cursor", "pointer");
        dialogClose.addClickListener(event -> close());

        Label nameOfDialog = new Label("Nowe wydarzenie");
        nameOfDialog.getStyle().set("margin-left", "auto");
        nameOfDialog.getStyle().set("margin-right", "auto");
        nameOfDialog.getStyle().set("font-size", "24px");

        TextField textField = new TextField();
        textField.setPlaceholder("Temat");
        textField.setWidth("50%");

        ComboBox<EventType> eventType = new ComboBox<>();
        eventType.setPlaceholder("Rodzaj");
        eventType.setAllowCustomValue(false);
        eventType.setItems(EventType.values());
        eventType.setRenderer(new TextRenderer<>(EventType::getDescription));
        eventType.setItemLabelGenerator(EventType::getDescription);
        eventType.setWidth("50%");

        HorizontalLayout dataFields = new HorizontalLayout(textField, eventType);
        dataFields.setWidth("100%");

        Div line = new Div();
        line.getStyle().set("background-color", "black");
        line.setHeight("3px");
        line.setWidth("100%");

        Button addButton = new Button("Dodaj!", e -> {
            EventDialogMethods eventDialogMethods = new EventDialogMethods();
            eventDialogMethods.onAdd(eventService, eventDate,
                    scheduleEntry.getHour(), eventType.getValue(), textField.getValue(), userID);
            close();
        });
        addButton.setWidth("100%");

        Div eventListLabel = new Div();
        eventListLabel.setText("Wydarzenia w tym dniu: ");

        eventGrid = new Grid<>();

        updateEvents();
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
                eventService.deleteEvent(new ScheduleEvent(userID, e.getId(), e.getDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
                        e.getHour(), e.getEventType(), e.getTopic(), e.getCreatedDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()));
                int groupCode = userService.getGroupCode(userID);
                String groupName = groupsService.getGroupName(groupCode);
                UUID groupLeaderId = groupsService.getLeaderId(groupName);

                updateEvents();
            });
            return icon;
        })).setHeader("Usuń");
        eventGrid.setSelectionMode(Grid.SelectionMode.NONE);
        eventGrid.setSizeFull();
        eventGrid.setVerticalScrollingEnabled(true);

        VerticalLayout mainLayout = new VerticalLayout(dialogClose, nameOfDialog, dataFields, addButton, line, eventListLabel, eventGrid);
        mainLayout.setSizeFull();

        setWidth("450px");
        setHeight("525px");
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

        add(mainLayout);
    }

    private void updateEvents() {
        List<Event> eventsSorted = new ArrayList<>(eventService.getEvents(startOfWeek, userID));
        eventsSorted.removeIf(event -> event.getDate().getDayOfWeek() != eventDate.getDayOfWeek());
        entries = scheduleService.getScheduleEntries(userID);
        if (eventsSorted.size() > 0) {
            eventGrid.setItems(eventsSorted);
        }
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