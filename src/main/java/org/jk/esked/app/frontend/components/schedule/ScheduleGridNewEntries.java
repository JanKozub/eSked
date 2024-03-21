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
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.backend.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScheduleGridNewEntries extends VerticalLayout { //TODO cleanup
    private final ScheduleEntryService scheduleEntryService;
    private final HourService hourService;
    private final Grid<Button> scheduleGrid;
    private final List<Button> buttons = new ArrayList<>();
    private final UUID userId;

    public ScheduleGridNewEntries(User user, ScheduleEntryService scheduleEntryService, UserService userService, HourService hourService) {
        this.scheduleEntryService = scheduleEntryService;
        this.hourService = hourService;
        this.userId = user.getId();

        scheduleGrid = new Schedule(userService, userId) {
            @Override
            Component rowRenderer(Button e, int day) {
                return ScheduleGridNewEntries.this.rowRenderer(user, day, Integer.parseInt(e.getText()));
            }

            @Override
            Component hourRenderer(Button e) {
                return ScheduleGridNewEntries.this.hourRenderer(user, Integer.parseInt(e.getText()));
            }
        };
        scheduleGrid.setAllRowsVisible(true);

        Button more = new Button(new Icon(VaadinIcon.ARROW_DOWN), event -> ScheduleGrid.addRow(buttons, scheduleGrid));
        more.setWidth("100%");

        for (int i = 0; i < hourService.getScheduleMaxHour(userId) + 1; i++)
            ScheduleGrid.addRow(buttons, scheduleGrid);

        setSizeFull();
        add(scheduleGrid, more);
    }

    private Button rowRenderer(User user, int day, int hour) {
        Button button = new Button(getTranslation("add"));
        button.setSizeFull();

        ScheduleEntry entry = scheduleEntryService.findByUserIdAndDayAndHour(user.getId(), day, hour);
        if (entry != null) {
            button.setText(entry.getSubject());
            button.addClickListener(clickEvent -> {
                DeleteEntryDialog dialog = new DeleteEntryDialog(user.getId(), scheduleEntryService, entry);
                dialog.addDetachListener(action -> refresh());
                dialog.open();
            });
            button.getStyle().set("color", "green");
            return button;
        }

        button.addClickListener(clickEvent -> {
            AddEntryDialog dialog = new AddEntryDialog(user, scheduleEntryService, hour, day);
            dialog.addDetachListener(action -> refresh());
            dialog.open();
        });
        button.getStyle().set("color", "red");
        return button;
    }

    private Button hourRenderer(User user, int scheduleHour) {
        String finalValue = String.valueOf(scheduleHour);
        Hour hour = hourService.getHourValueByHour(userId, scheduleHour);
        if (hour != null) finalValue = hour.getData();

        return new Button(finalValue, h -> {
            Dialog dialog;
            if (hourService.getHourValueByHour(userId, scheduleHour) != null) {
                dialog = new DeleteHourDialog(hourService, h.getSource(), user, scheduleHour);
            } else {
                dialog = new AddHourDialog(hourService, h.getSource(), user, scheduleHour);
            }
            dialog.addDetachListener(d -> refresh());
        });
    }

    private void refresh() {
        ListDataProvider<Button> dataProvider = new ListDataProvider<>(buttons);
        scheduleGrid.setItems(dataProvider);
    }
}
