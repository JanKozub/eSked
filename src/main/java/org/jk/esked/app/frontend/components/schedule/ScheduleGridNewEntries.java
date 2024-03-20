package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.jk.esked.app.backend.model.entities.Hour;
import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.fields.TimePicker24h;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ScheduleGridNewEntries extends VerticalLayout {//TODO code cleanup
    private final ScheduleService scheduleService;
    private final HourService hourService;
    private final Grid<Button> scheduleGrid;
    private final List<Button> buttons = new ArrayList<>();
    private final UUID userId;
    private Collection<ScheduleEntry> entries;

    public ScheduleGridNewEntries(User user, ScheduleService scheduleService, UserService userService, HourService hourService) { //TODO unused??
        this.scheduleService = scheduleService;
        this.hourService = hourService;
        this.userId = user.getId();

        entries = scheduleService.getScheduleEntriesByUserId(userId);

        scheduleGrid = new Schedule(userService, userId) {
            @Override
            Component rowRenderer(Button e, int day) {
                return ScheduleGridNewEntries.this.rowRenderer(user, e, day);
            }

            @Override
            Component hourRenderer(Button e) {
                return ScheduleGridNewEntries.this.hourRenderer(user, e);
            }
        };
        scheduleGrid.setAllRowsVisible(true);

        Button more = new Button(new Icon(VaadinIcon.ARROW_DOWN), event -> ScheduleGrid.addRow(buttons, scheduleGrid));
        more.setWidth("100%");

        for (int i = 0; i < hourService.getScheduleMaxHour(userId) + 1; i++) ScheduleGrid.addRow(buttons, scheduleGrid);

        add(scheduleGrid);
        setSizeFull();
        add(more);
    }

    private Button rowRenderer(User user, Button e, int day) {
        Button button = new Button(getTranslation("add"));
        button.setSizeFull();
        for (ScheduleEntry entry : entries) {
            if (entry.getHour() == Integer.parseInt(e.getText()) && entry.getDay() == day) {
                button.setText(entry.getSubject());
                button.addClickListener(clickEvent -> {
                    DeleteEntryDialog dialog = new DeleteEntryDialog(user.getId(), scheduleService, entry);
                    dialog.addDetachListener(action -> refresh());
                    dialog.open();
                });
                button.getStyle().set("color", "green");
                return button;
            }
        }
        button.addClickListener(clickEvent -> {
            AddEntryDialog dialog = new AddEntryDialog(user, scheduleService, Integer.parseInt(e.getText()), day);
            dialog.addDetachListener(action -> refresh());
            dialog.open();
        });
        button.getStyle().set("color", "red");
        return button;
    }

    private Button hourRenderer(User user, Button e) {
        int value = Integer.parseInt(e.getText()) + 1;
        String finalValue = String.valueOf(value);
        Hour hour = hourService.getHourValueByHour(userId, value);
        if (hour != null) finalValue = hour.getData();

        return new Button(finalValue, h -> {
            Dialog dialog = new Dialog();
            dialog.setCloseOnOutsideClick(true);
            dialog.addDetachListener(d -> refresh());

            if (hourService.getHourValueByHour(userId, value) != null) { //TODO translations
                Button delete =  new Button("delete", s -> {
                    hourService.deleteHourByUserIdAndHour(userId, value);

                    h.getSource().setText(String.valueOf(value));
                    dialog.close();
                });
                dialog.add(delete);
            } else {
                TimePicker24h timePicker =  new TimePicker24h();
                Button submit = new Button("submit", s -> {
                    if (hourService.getHourValueByHour(user.getId(), value) != null) return;

                    Hour newHour = new Hour();
                    newHour.setUser(user);
                    newHour.setHour(value);
                    newHour.setData(timePicker.getValueIn24h());
                    hourService.saveHour(newHour);
                    h.getSource().setText(timePicker.getValueIn24h());
                    dialog.close();
                });
                dialog.add(new VerticalLayout(timePicker, submit));
            }
            dialog.open();
        });
    }

    private void refresh() {
        entries = scheduleService.getScheduleEntriesByUserId(userId);
        ListDataProvider<Button> dataProvider = new ListDataProvider<>(buttons);
        scheduleGrid.setItems(dataProvider);
    }
}
