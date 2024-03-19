package org.jk.esked.app.frontend.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jk.esked.app.backend.security.SecurityService;

@CssImport("./styles/styles.css")
public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        Image logo = new Image("icons/icon.png", "logo");
        logo.setHeight("44px");
        logo.getStyle().set("padding", "10px");
        H1 title = new H1("eSked");
        title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM);

        Button settings = new Button(VaadinIcon.COG_O.create(), e -> UI.getCurrent().navigate("settings"));
        header.add(logo, title, settings);

        if (securityService.getAuthenticatedUser().getUsername().equals("admin")) //TODO implement roles
            header.add(new Button(VaadinIcon.CALC_BOOK.create(), e -> UI.getCurrent().navigate("admin")));

        Button logout = new Button(VaadinIcon.POWER_OFF.create(), e -> securityService.logout());
        header.add(logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(title);
        header.setWidthFull();
        header.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {
        Tab schedule = new Tab(VaadinIcon.CALENDAR_O.create(),
                new RouterLink(getTranslation("tab.schedule"), ScheduleView.class));
        schedule.getStyle().set("font-size", "var(--lumo-font-size-l)");
        Tab newScheduleEntry = new Tab(VaadinIcon.FILE_ADD.create(),
                new RouterLink(getTranslation("tab.schedule.new"), NewScheduleEntryView.class));
        newScheduleEntry.getStyle().set("font-size", "var(--lumo-font-size-l)");
        Tab events = new Tab(VaadinIcon.CALENDAR_CLOCK.create(),
                new RouterLink(getTranslation("tab.events"), EventsView.class));
        events.getStyle().set("font-size", "var(--lumo-font-size-l)");
        Tab newEvent = new Tab(VaadinIcon.FOLDER_ADD.create(),
                new RouterLink(getTranslation("tab.events.new"), NewEventView.class));
        newEvent.getStyle().set("font-size", "var(--lumo-font-size-l)");
        Tab messages = new Tab(VaadinIcon.ENVELOPE_OPEN_O.create(),
                new RouterLink(getTranslation("tab.messages"), MessagesView.class));
        messages.getStyle().set("font-size", "var(--lumo-font-size-l)");

        addToDrawer(schedule, newScheduleEntry, events, newEvent, messages);
    }
}
