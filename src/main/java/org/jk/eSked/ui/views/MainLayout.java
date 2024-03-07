package org.jk.eSked.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
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
        H1 title = new H1("eSked");
        title.getStyle().set("font-size", "var(--lumo-font-size-xxl)").set("margin", "0");
        Image logo = new Image("icons/icon.png", "logo");
        logo.setHeight("44px");
        logo.getStyle().set("padding", "10px");

        addToDrawer(getTabs());
        addToNavbar(logo, title, createMenuBar());
    }

    private Tab[] getTabs() {
        Tab schedule = new Tab(VaadinIcon.CALENDAR_O.create(), new RouterLink("Plan", ScheduleView.class));
        schedule.getStyle().set("font-size", "var(--lumo-font-size-l)");
        Tab events = new Tab(VaadinIcon.CALENDAR_CLOCK.create(), new RouterLink("Wydarzenia", EventsView.class));
        events.getStyle().set("font-size", "var(--lumo-font-size-l)");
        Tab newEvent = new Tab(VaadinIcon.FOLDER_ADD.create(), new RouterLink("Dodaj Wydarzenie", NewEventView.class));
        newEvent.getStyle().set("font-size", "var(--lumo-font-size-l)");
        Tab messages = new Tab(VaadinIcon.ENVELOPE_OPEN_O.create(), new RouterLink("Wiadomości", MessagesView.class));
        messages.getStyle().set("font-size", "var(--lumo-font-size-l)");

        return new Tab[]{schedule, events, newEvent, messages};
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getStyle().set("margin-left", "auto").set("padding", "15px");
        menuBar.addItem(VaadinIcon.COG_O.create(), e -> UI.getCurrent().navigate("settings"));

        Secured secured = AnnotationUtils.findAnnotation(AdminView.class, Secured.class);
        if (secured != null) addAdminTab(secured, menuBar);
        else log.error("@secured annotation is equal null");

        menuBar.addItem(VaadinIcon.POWER_OFF.create(), e -> logout());

        return menuBar;
    }

    private void addAdminTab(Secured secured, MenuBar menuBar) {
        List<String> allowedRoles = Arrays.asList(secured.value());
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (userAuthentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(allowedRoles::contains)) {
            menuBar.addItem(VaadinIcon.CALC_BOOK.create(), e -> UI.getCurrent().navigate("admin"));
        }
    }

    private void logout() {
        UI.getCurrent().navigate("login");
        SecurityContextHolder.clearContext();
        VaadinSession.getCurrent().close();
    }
}
