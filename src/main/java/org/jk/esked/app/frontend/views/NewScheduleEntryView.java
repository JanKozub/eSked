package org.jk.esked.app.frontend.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.schedule.ScheduleGridNewEntries;

@PermitAll
@Route(value = "schedule/new", layout = MainLayout.class)
public class NewScheduleEntryView extends VerticalLayout implements HasDynamicTitle {
    public NewScheduleEntryView(SecurityService securityService, ScheduleService scheduleService, UserService userService) {
        add(new ScheduleGridNewEntries(securityService.getUser(), scheduleService, userService));
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.schedule.new");
    }
}
