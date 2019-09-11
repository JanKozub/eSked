package org.jk.eSked.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
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
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleGridNewEntries extends VerticalLayout {

    private final ScheduleService scheduleService;
    private final GroupsService groupsService;
    private LocalDate startOfWeek;
    private final Grid<Button> scheduleGrid;
    private final List<Button> buttons = new ArrayList<>();
    private final int type;
    private final UUID userId;
    private Collection<Entry> entries;

    public ScheduleGridNewEntries(ScheduleService scheduleService, GroupsService groupsService, UserService userService, int type) {
        this.scheduleService = scheduleService;
        this.groupsService = groupsService;
        this.userId = VaadinSession.getCurrent().getAttribute(User.class).getId();
        this.type = type;

        entries = scheduleService.getEntries(userId);

        if (startOfWeek == null) startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

        scheduleGrid = new Schedule(userService, userId) {
            @Override
            Component rowRenderer(Button e, int day) {
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

            @Override
            Component hourRenderer(Button e) {
                return new Label(Integer.toString(Integer.parseInt(e.getText()) + 1));
            }
        };

        Button more = new Button(new Icon(VaadinIcon.ARROW_DOWN), event -> addRow());
        more.setWidth("100%");

        for (int i = 0; i < getMaxHour(); i++) addRow();

        if (VaadinSession.getCurrent().getBrowser().getBrowserApplication().contains("Mobile")) {
            setMobileColumns(1);
            AtomicInteger triggeredColumn = new AtomicInteger(1);
            Button next = new Button(VaadinIcon.ARROW_RIGHT.create(), nextColumn -> {
                if (triggeredColumn.get() != 5) triggeredColumn.set(triggeredColumn.get() + 1);
                setMobileColumns(triggeredColumn.get());
            });
            next.setWidth("100%");
            Button prev = new Button(VaadinIcon.ARROW_LEFT.create(), prevColumn -> {
                if (triggeredColumn.get() != 1) triggeredColumn.set(triggeredColumn.get() - 1);
                setMobileColumns(triggeredColumn.get());
            });
            prev.setWidth("100%");
            HorizontalLayout layout = new HorizontalLayout(prev, next);
            layout.setWidth("100%");
            add(scheduleGrid, layout);
        } else add(scheduleGrid);
        add(more);
    }

    private void setMobileColumns(int pos) {
        for (int i = 1; i < 6; i++) {
            if (i == pos) scheduleGrid.getColumnByKey(Integer.toString(i)).setVisible(true);
            else scheduleGrid.getColumnByKey(Integer.toString(i)).setVisible(false);
        }
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

    private void newEntryDialog(int day, int hour) {
        Dialog dialog = new Dialog();

        Label label = new Label("Nowy przedmiot");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems("INF", "J.Niem", "J.ANG", "J.POL", "MAT", "CHEM", "BIOL",
                "FIZ", "GEO", "WF", "REL", "HIST", "EDB", "WOS", "PLA", "G.W.");
        comboBox.setPlaceholder("Wybierz przedmiot");
        comboBox.setWidth("100%");

        TextField textField = new TextField();
        textField.setPlaceholder("Własny przedmiot");

        HorizontalLayout layout = new HorizontalLayout(comboBox, textField);

        Button addButton = new Button("Dodaj!", event -> {
            String value = "";
            if (comboBox.getValue() != null && !comboBox.getValue().equals("")) {
                value = comboBox.getValue();
            }

            if (!textField.getValue().equals("")) {
                value = textField.getValue();
            }

            if (!value.equals("")) {
                if (comboBox.getValue() != null && !comboBox.getValue().equals("") && !textField.getValue().equals("")) {
                    comboBox.setErrorMessage("Można wybrać tylko jedna opcje");
                    comboBox.setInvalid(true);
                    textField.setInvalid(true);
                    comboBox.clear();
                    textField.clear();
                } else {
                    addEvent(day, hour, value, dialog);
                }
            } else {
                comboBox.setErrorMessage("Jedno z pól nie moze być puste");
                textField.setInvalid(true);
                comboBox.setInvalid(true);
            }
        });

        addButton.setWidth("100%");

        VerticalLayout verticalLayout = new VerticalLayout(label, layout, addButton);
        verticalLayout.setAlignItems(Alignment.CENTER);

        dialog.add(verticalLayout);
        dialog.open();
    }

    private void addEvent(int day, int hour, String value, Dialog dialog) {
        entries = scheduleService.getEntries(userId);
        boolean create = true;
        for (Entry entry : entries) {
            if (entry.getHour() == hour && entry.getDay() == day) {
                create = false;
                break;
            }
        }
        Entry entry = new Entry(hour, day, value, Instant.now().toEpochMilli());
        if (create) {
            if (type == 0)
                scheduleService.addScheduleEntry(
                        new ScheduleEntry(userId, entry.getHour(), entry.getDay(), entry.getSubject(), Instant.now().toEpochMilli()));
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
    }

    private void confirmDialog(Dialog dialog2, Entry entry) {
        Dialog dialog = new Dialog();
        Label label = new Label("Istnieje juz wpis na tym miejscu, usunąc i wprowadzić nowy?");
        Button yes = new Button("Tak");
        yes.addClickListener(event -> {
            if (type == 0) {
                ScheduleEntry scheduleEntry = new ScheduleEntry(userId, entry.getHour(), entry.getDay(), entry.getSubject(), Instant.now().toEpochMilli());
                scheduleService.deleteScheduleEntry(userId, entry.getHour(), entry.getDay());
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
                scheduleService.deleteScheduleEntry(userId, entry.getHour(), entry.getDay());
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
        entries = scheduleService.getEntries(userId);
        ListDataProvider<Button> dataProvider = new ListDataProvider<>(buttons);
        scheduleGrid.setDataProvider(dataProvider);
    }
}
