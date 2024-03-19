package org.jk.esked.app.frontend.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.frontend.components.DrawerTab;

@CssImport("styles/styles.css")
public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        HorizontalLayout header = new HorizontalLayout(new Image("icons/icon.png", "logo"), new H1("eSked"));
        header.addClassName("header");

        Button settings = new Button(VaadinIcon.COG_O.create(), e -> UI.getCurrent().navigate("settings"));
        HorizontalLayout buttons = new HorizontalLayout(settings);
        buttons.addClassName("buttons");

        if (securityService.getAuthenticatedUser().getUsername().equals("admin")) //TODO implement roles
            buttons.add(new Button(VaadinIcon.CALC_BOOK.create(), e -> UI.getCurrent().navigate("admin")));

        buttons.add(new Button(VaadinIcon.POWER_OFF.create(), e -> securityService.logout()));

        addToNavbar(header, buttons);
    }

    private void createDrawer() {
        addToDrawer(
                new DrawerTab(VaadinIcon.CALENDAR_O, getTranslation("tab.schedule"), ScheduleView.class),
                new DrawerTab(VaadinIcon.FILE_ADD, getTranslation("tab.schedule.new"), NewScheduleEntryView.class),
                new DrawerTab(VaadinIcon.CALENDAR_CLOCK, getTranslation("tab.events"), EventsView.class),
                new DrawerTab(VaadinIcon.FOLDER_ADD, getTranslation("tab.events.new"), NewEventView.class),
                new DrawerTab(VaadinIcon.ENVELOPE_OPEN_O, getTranslation("tab.messages"), MessagesView.class)
        );
    }
}
