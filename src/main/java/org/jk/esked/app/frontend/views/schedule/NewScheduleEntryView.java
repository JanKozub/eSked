package org.jk.esked.app.frontend.views.schedule;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.schedule.ScheduleGridNewEntries;
import org.jk.esked.app.frontend.views.MainLayout;

@PermitAll
@Route(value = "schedule/new", layout = MainLayout.class)
public class NewScheduleEntryView extends VerticalLayout implements HasDynamicTitle {
    public NewScheduleEntryView(SecurityService securityService, ScheduleEntryService scheduleEntryService, UserService userService, HourService hourService) {
        add(new ScheduleGridNewEntries(securityService.getUser(), scheduleEntryService, userService, hourService));
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.schedule.new");
    }
}
