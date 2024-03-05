package org.jk.eSked.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.ui.views.admin.AdminView;
import org.jk.eSked.ui.views.events.EventsView;
import org.jk.eSked.ui.views.events.NewEventView;
import org.jk.eSked.ui.views.messages.MessagesView;
import org.jk.eSked.ui.views.schedule.ScheduleView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

public class MainLayout extends AppLayout implements RouterLayout {
    private static final Logger log = LoggerFactory.getLogger(MainLayout.class);

    public MainLayout() {
        Tab schedule = new Tab(VaadinIcon.CALENDAR_O.create(), new RouterLink("Plan", ScheduleView.class));
        Tab events = new Tab(VaadinIcon.CALENDAR_CLOCK.create(), new RouterLink("Wydarzenia", EventsView.class));
        Tab newEvent = new Tab(VaadinIcon.FOLDER_ADD.create(), new RouterLink("Dodaj Wydarzenie", NewEventView.class));
        Tab messages = new Tab(VaadinIcon.ENVELOPE_OPEN_O.create(), new RouterLink("Wiadomości", MessagesView.class));

        addToDrawer(schedule, events, newEvent, messages);

        MenuBar menuBar = new MenuBar();

        menuBar.addItem(VaadinIcon.COG_O.create(), e -> UI.getCurrent().navigate("settings"));

        Secured secured = AnnotationUtils.findAnnotation(AdminView.class, Secured.class);
        if (secured != null) {
            List<String> allowedRoles = Arrays.asList(secured.value());
            Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
            if (userAuthentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(allowedRoles::contains)) {
                menuBar.addItem(VaadinIcon.CALC_BOOK.create(), e -> UI.getCurrent().navigate("admin"));
            }
        }
        else log.error("@secured annotation is equal null");
        menuBar.addItem(VaadinIcon.POWER_OFF.create(), e -> logout());

        addToNavbar(menuBar);
    }

    private void logout() {
        UI.getCurrent().navigate("login");
        SecurityContextHolder.clearContext();
        VaadinSession.getCurrent().close();
    }
}
