package org.jk.eSked.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.model.User;
import org.jk.eSked.model.entry.Entry;
import org.jk.eSked.model.entry.ScheduleEntry;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ScheduleGridNewEntries extends VerticalLayout {

    private final ScheduleService scheduleService;
    private final GroupsService groupsService;
    private LocalDate startOfWeek;
    private final Grid<Button> scheduleGrid;
    private final List<Button> buttons = new ArrayList<>();
    private final int type;
    private final UUID userID;
    private Collection<Entry> entries;

    public ScheduleGridNewEntries(ScheduleService scheduleService, GroupsService groupsService, UserService userService, int type) {
        this.scheduleService = scheduleService;
        this.groupsService = groupsService;
        this.userID = VaadinSession.getCurrent().getAttribute(User.class).getId();
        this.type = type;

        entries = scheduleService.getEntries(userID);

        if (startOfWeek == null) {
            startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        }

        scheduleGrid = new Grid<>();

        if (userService.getScheduleHours(userID))
            scheduleGrid.addColumn(new ComponentRenderer<>(e -> new Label(Integer.toString(Integer.parseInt(e.getText()) + 1)))).setHeader("G|D").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0);
        scheduleGrid.addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 0))).setHeader("Poniedziałek").setTextAlign(ColumnTextAlign.CENTER);
        scheduleGrid.addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 1))).setHeader("Wtorek").setTextAlign(ColumnTextAlign.CENTER);
        scheduleGrid.addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 2))).setHeader("Środa").setTextAlign(ColumnTextAlign.CENTER);
        scheduleGrid.addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 3))).setHeader("Czwartek").setTextAlign(ColumnTextAlign.CENTER);
        scheduleGrid.addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 4))).setHeader("Piątek").setTextAlign(ColumnTextAlign.CENTER);
        scheduleGrid.setSelectionMode(Grid.SelectionMode.NONE);
        scheduleGrid.setHeightByRows(true);
        scheduleGrid.setVerticalScrollingEnabled(true);

        Button less = new Button(new Icon(VaadinIcon.ARROW_UP), event -> removeRow());
        less.setWidth("100%");
        Button more = new Button(new Icon(VaadinIcon.ARROW_DOWN), event -> addRow());
        more.setWidth("100%");

        for (int i = 0; i < getMaxHour(); i++) addRow();

        add(scheduleGrid, less, more);
    }

    private Component rowRenderer(Button e, int day) {
        Button button = new Button("Dodaj");
        button.setSizeFull();
        for (Entry entry : entries) {
            if (entry.getHour() == Integer.parseInt(e.getText()) && entry.getDay() == day) {
                button.setText(entry.getSubject());
                button.addClickListener(clickEvent -> deleteEntryDialog(entry));
                button.getStyle().set("color", "green");
                return button;
            }
        }
        button.addClickListener(clickEvent -> newEntryDialog(day, Integer.parseInt(e.getText())));
        button.getStyle().set("color", "red");
        return button;
    }

    private int getMaxHour() {
        int maxHour = 0;
        for (Entry entry : entries) {
            if (entry.getHour() > maxHour) maxHour = entry.getHour();
        }
        return maxHour + 1;
    }

    private void addRow() {
        int size = buttons.size();
        if (size > 0) {
            int maxNum = Integer.parseInt(buttons.get(size - 1).getText()) + 1;
            buttons.add(new Button(Integer.toString(maxNum)));
        } else buttons.add(new Button("0"));
        scheduleGrid.setItems(buttons);
    }

    private void removeRow() {
        int size = buttons.size();
        if (size > 0) {
            int maxNum = Integer.parseInt(buttons.get(size - 1).getText());
            buttons.remove(maxNum);
        }
        scheduleGrid.setItems(buttons);
    }

    private void newEntryDialog(int day, int hour) {
        Dialog dialog = new Dialog();


        ComboBox<String> textField = new ComboBox<>();
        textField.isAllowCustomValue();
        textField.setItems("INF", "J.Niem", "J.ANG", "J.POL", "MAT", "CHEM", "BIOL",
                "FIZ", "GEO", "WF", "REL", "HIST", "EDB", "WOS", "PLA", "G.W.");
        textField.setPlaceholder("Wpisz/ustaw nazwe przedmiotu");
        textField.setWidth("100%");

        Button addButton = new Button("Dodaj!", event -> {
            if (textField.getValue() != null && !textField.getValue().equals("")) {
                textField.setInvalid(false);
                entries = scheduleService.getEntries(userID);
                boolean create = true;
                for (Entry entry : entries) {
                    if (entry.getHour() == hour && entry.getDay() == day) {
                        create = false;
                        break;
                    }
                }
                Entry entry = new Entry(hour, day, textField.getValue(), Instant.now().toEpochMilli());
                if (create) {
                    if (type == 0)
                        scheduleService.addScheduleEntry(
                                new ScheduleEntry(userID, entry.getHour(), entry.getDay(), entry.getSubject(), Instant.now().toEpochMilli()));
                    else {
                        String groupName = groupsService.getGroupName(type);
                        groupsService.addEntryToGroup(true, groupName, type,
                                groupsService.getLeaderId(groupName),
                                entry.getHour(), entry.getDay(), entry.getSubject(),
                                entry.getCreatedDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
                    }

                    dialog.close();
                    refresh();
                } else {
                    confirmDialog(dialog, entry);
                }
            } else {
                textField.setErrorMessage("Pole nie może być puste");
                textField.setInvalid(true);
            }
        });
        addButton.setWidth("100%");

        VerticalLayout verticalLayout = new VerticalLayout(textField, addButton);
        verticalLayout.setAlignItems(Alignment.CENTER);

        dialog.add(verticalLayout);
        dialog.open();
    }

    private void confirmDialog(Dialog dialog2, Entry entry) {
        Dialog dialog = new Dialog();
        Label label = new Label("Istnieje juz wpis na tym miejscu, usunąc i wprowadzić nowy?");
        Button yes = new Button("Tak");
        yes.addClickListener(event -> {
            if (type == 0) {
                ScheduleEntry scheduleEntry = new ScheduleEntry(userID, entry.getHour(), entry.getDay(), entry.getSubject(), Instant.now().toEpochMilli());
                scheduleService.deleteScheduleEntry(userID, entry.getHour(), entry.getDay());
                scheduleService.addScheduleEntry(scheduleEntry);
            } else {
                groupsService.deleteGroupEntry(type, entry.getHour(), entry.getDay());
                String groupName = groupsService.getGroupName(type);
                groupsService.addEntryToGroup(true, groupName, type, groupsService.getLeaderId(groupName), entry.getHour(), entry.getDay(), entry.getSubject(),
                        entry.getCreatedDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
            }
            scheduleGrid.setItems(buttons);
            dialog.close();
            dialog2.close();
        });
        Button no = new Button("Nie", event -> dialog.close());
        HorizontalLayout horizontalLayout = new HorizontalLayout(yes, no);
        horizontalLayout.setSizeFull();
        yes.setWidth("50%");
        no.setWidth("50%");
        VerticalLayout layout = new VerticalLayout(label, horizontalLayout);
        dialog.add(layout);
        dialog.open();
    }

    private void deleteEntryDialog(Entry entry) {
        Dialog dialog = new Dialog();

        Label label = new Label("Aktualny przedmiot:");
        Label name = new Label(entry.getSubject());

        Button deleteButton = new Button("Usuń!", event -> {
            if (type == 0) {
                scheduleService.deleteScheduleEntry(userID, entry.getHour(), entry.getDay());
            } else {
                groupsService.deleteGroupEntry(type, entry.getHour(), entry.getDay());
            }
            dialog.close();
            refresh();
        });
        deleteButton.setWidth("100%");

        VerticalLayout layout = new VerticalLayout(label, name, deleteButton);
        layout.setAlignItems(Alignment.CENTER);

        dialog.add(layout);
        dialog.open();
    }

    private void refresh() {
        entries = scheduleService.getEntries(userID);
        ListDataProvider<Button> dataProvider = new ListDataProvider<>(buttons);
        scheduleGrid.setDataProvider(dataProvider);
    }
}
