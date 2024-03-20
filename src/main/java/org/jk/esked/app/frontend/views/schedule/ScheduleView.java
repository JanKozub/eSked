package org.jk.esked.app.frontend.views.schedule;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.EventService;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.schedule.ScheduleGrid;
import org.jk.esked.app.frontend.views.MainLayout;
import org.springframework.context.annotation.Scope;

@SpringComponent
@PermitAll
@Scope("prototype")
@Route(value = "", layout = MainLayout.class)
public class ScheduleView extends VerticalLayout implements HasDynamicTitle {
    public ScheduleView(SecurityService securityService, ScheduleService scheduleService, EventService eventService, UserService userService, HourService hoursService) {
        add(new ScheduleGrid(securityService.getUser(), scheduleService, eventService, userService, hoursService));
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.schedule");
    }
}
