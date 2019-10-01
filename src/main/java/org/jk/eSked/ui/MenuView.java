package org.jk.eSked.ui;

import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import org.jk.eSked.ui.views.admin.AdminView;
import org.jk.eSked.ui.views.events.EventsView;
import org.jk.eSked.ui.views.events.NewEventView;
import org.jk.eSked.ui.views.schedule.ScheduleView;
import org.springframework.stereotype.Component;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Component
@UIScope
public class MenuView extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> {
    private DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private DefaultBadgeHolder badge = new DefaultBadgeHolder(5);

    public MenuView() {
        notifications.addClickListener(notification -> {
        });
        MenuBar menuBar = new MenuBar();

        menuBar.addItem(VaadinIcon.COG_O.create(), e -> UI.getCurrent().navigate("settings"));
        menuBar.addItem(VaadinIcon.POWER_OFF.create(), e -> logout());

        LeftNavigationItem[] tab = tabs();

        init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
                .withTitle("eSked")
                .withAppBar(AppBarBuilder.get()
                        .add(menuBar)
                        .build())
                .withAppMenu(LeftAppMenuBuilder.get()
                        .add(tab[0], tab[1], tab[2], tab[3])
                        .build())
                .build());
    }

    private LeftNavigationItem[] tabs() {
        LeftNavigationItem schedule = new LeftNavigationItem("Plan", VaadinIcon.CALENDAR_O.create(), ScheduleView.class);
        LeftNavigationItem events = new LeftNavigationItem("Wydarzenia", VaadinIcon.CALENDAR_CLOCK.create(), EventsView.class);
        badge.bind(events.getBadge());
        LeftNavigationItem newEvent = new LeftNavigationItem("Dodaj Wydarzenie", VaadinIcon.FOLDER_ADD.create(), NewEventView.class);
        LeftNavigationItem admin = new LeftNavigationItem("Admininstracja", VaadinIcon.CALC_BOOK.create(), AdminView.class);
        return new LeftNavigationItem[]{schedule, events, newEvent, admin};
    }

    public DefaultNotificationHolder getNotifications() {
        return notifications;
    }

    public DefaultBadgeHolder getBadge() {
        return badge;
    }

    private void logout() {
        UI.getCurrent().navigate("login");
        VaadinSession.getCurrent().close();
        UI.getCurrent().getPage().reload();
    }
}