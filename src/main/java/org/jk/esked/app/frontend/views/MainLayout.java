package org.jk.esked.app.frontend.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.jk.esked.app.backend.model.types.UserType;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.frontend.components.other.DrawerTab;
import org.jk.esked.app.frontend.views.events.EventsView;
import org.jk.esked.app.frontend.views.events.NewEventView;
import org.jk.esked.app.frontend.views.other.MessagesView;
import org.jk.esked.app.frontend.views.schedule.NewScheduleEntryView;
import org.jk.esked.app.frontend.views.schedule.ScheduleView;

@CssImport("./styles/styles.css")
public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        HorizontalLayout header = new HorizontalLayout(new SvgIcon("icons/logo.svg"), new Span("eSked"));
        header.addClassName("header");

        addToNavbar(header, createButtons());
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout buttons = new HorizontalLayout(new Button(getTranslation("page.settings"), VaadinIcon.COG_O.create(), e -> UI.getCurrent().navigate("settings")));
        buttons.addClassName("header-buttons");

        if (securityService.getUser().getUserType() == UserType.ADMIN)
            buttons.add(new Button("Manager", VaadinIcon.CALC_BOOK.create(), e -> UI.getCurrent().navigate("admin")));

        buttons.add(new Button(getTranslation("log.out"), VaadinIcon.POWER_OFF.create(), e -> securityService.logout()));
        return buttons;
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
