package org.jk.eSked.ui.views.schedule;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.ScheduleService;
import org.jk.eSked.backend.service.UserService;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.schedule.ScheduleGridNewEntries;

@Route(value = "schedule/new", layout = MenuView.class)
@PageTitle("Nowy Wpis Do Planu")
public class NewEntryView extends VerticalLayout {

    public NewEntryView(ScheduleService scheduleService, UserService userService) {
        VerticalLayout scheduleGrid = new ScheduleGridNewEntries(scheduleService, userService);
        add(scheduleGrid);
    }
}