package org.jk.esked.app.ui.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.jk.esked.app.ui.components.scheduleDialogs.AddEntryDialog;
import org.jk.esked.app.ui.components.scheduleDialogs.DeleteEntryDialog;
import org.jk.esked.app.backend.model.ScheduleEntry;
import org.jk.esked.app.backend.model.User;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ScheduleGridNewEntries extends VerticalLayout {
    private final ScheduleService scheduleService;
    private final Grid<Button> scheduleGrid;
    private final List<Button> buttons = new ArrayList<>();
    private final UUID userId;
    private Collection<ScheduleEntry> entries;

    public ScheduleGridNewEntries(User user, ScheduleService scheduleService, UserService userService) { //TODO unused??
        this.scheduleService = scheduleService;
        this.userId = user.getId();

        entries = scheduleService.getScheduleEntriesByUserId(userId);

        scheduleGrid = new Schedule(userService, userId) {
            @Override
            Component rowRenderer(Button e, int day) {
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

            @Override
            Component hourRenderer(Button e) {
                return new Text(Integer.toString(Integer.parseInt(e.getText()) + 1));
            }
        };
        scheduleGrid.setAllRowsVisible(true);

        Button more = new Button(new Icon(VaadinIcon.ARROW_DOWN), event -> ScheduleGrid.addRow(buttons, scheduleGrid));
        more.setWidth("100%");

        for (int i = 0; i < getMaxHour(); i++) ScheduleGrid.addRow(buttons, scheduleGrid);

        add(scheduleGrid);
        setSizeFull();
        add(more);
    }

    private int getMaxHour() {
        int maxHour = 0;
        for (ScheduleEntry entry : entries) maxHour = Math.max(maxHour, entry.getHour());

        return maxHour + 1;
    }

    private void refresh() {
        entries = scheduleService.getScheduleEntriesByUserId(userId);
        ListDataProvider<Button> dataProvider = new ListDataProvider<>(buttons);
        scheduleGrid.setItems(dataProvider);
    }
}
