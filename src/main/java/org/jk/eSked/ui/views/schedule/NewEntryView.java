package org.jk.eSked.ui.views.schedule;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.schedule.ScheduleGridNewEntries;
import org.jk.eSked.ui.views.MainLayout;

@Route(value = "schedule/new", layout = MainLayout.class)
public class NewEntryView extends VerticalLayout implements HasDynamicTitle {
    public NewEntryView(ScheduleService scheduleService, UserService userService) {
        SessionService.setAutoTheme();
        VerticalLayout scheduleGrid = new ScheduleGridNewEntries(scheduleService, userService);
        add(scheduleGrid);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.schedule.new");
    }
}
