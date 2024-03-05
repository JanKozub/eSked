package org.jk.eSked.ui.views.schedule;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.MainLayout;
import org.jk.eSked.ui.components.schedule.ScheduleGridNewEntries;

@Route(value = "schedule/new", layout = MainLayout.class)
@PageTitle("Nowy Wpis Do Planu")
public class NewEntryView extends VerticalLayout {

    public NewEntryView(ScheduleService scheduleService, UserService userService) {
        SessionService.setAutoTheme();
        VerticalLayout scheduleGrid = new ScheduleGridNewEntries(scheduleService, userService);
        add(scheduleGrid);
    }
}
