package org.jk.esked.app.frontend.views.schedule;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.theme.lumo.Lumo;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.EventService;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.backend.services.utilities.ThemeService;
import org.jk.esked.app.frontend.components.schedule.ScheduleGrid;
import org.jk.esked.app.frontend.views.MainLayout;
import org.springframework.context.annotation.Scope;

@SpringComponent
@PermitAll
@Scope("prototype")
@Route(value = "", layout = MainLayout.class)
@CssImport("./styles/schedule.css")
public class ScheduleView extends VerticalLayout implements HasDynamicTitle {
    public ScheduleView(SecurityService securityService, ScheduleEntryService scheduleEntryService, EventService eventService, UserService userService, HourService hoursService) {
        ThemeService.setTheme(securityService.getUser().isDarkTheme() ? Lumo.DARK : Lumo.LIGHT);
        add(new ScheduleGrid(securityService.getUser(), scheduleEntryService, eventService, userService, hoursService));
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.schedule");
    }
}
