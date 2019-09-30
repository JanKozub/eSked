package org.jk.eSked.ui;

import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.addons.notification.component.NotificationButton;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.github.appreciated.app.layout.entity.Section;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.spring.annotation.UIScope;
import org.jk.eSked.ui.views.AboutAppView;
import org.jk.eSked.ui.views.SettingsView;
import org.jk.eSked.ui.views.events.EventsView;
import org.jk.eSked.ui.views.events.NewEventView;
import org.jk.eSked.ui.views.schedule.NewEntryView;
import org.jk.eSked.ui.views.schedule.ScheduleView;
import org.springframework.stereotype.Component;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Component
@UIScope // optional but useful; allows access to this instance from views, see View1.
public class MenuView extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> {
    private DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private DefaultBadgeHolder badge = new DefaultBadgeHolder(5);

    public MenuView() {
        notifications.addClickListener(notification -> {/* ... */});

        LeftNavigationItem menuEntry = new LeftNavigationItem("Menu", VaadinIcon.MENU.create(), SettingsView.class);
        badge.bind(menuEntry.getBadge());

        init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
                .withTitle("App Layout")
                .withAppBar(AppBarBuilder.get()
                        .add(new NotificationButton<>(VaadinIcon.BELL, notifications))
                        .build())
                .withAppMenu(LeftAppMenuBuilder.get()
                        .addToSection(Section.HEADER,
                                new LeftHeaderItem("Menu-Header", "Version 4.0.0", "/frontend/images/logo.png"),
                                new LeftClickableItem("Clickable Entry", VaadinIcon.COG.create(), clickEvent -> Notification.show("onClick ..."))
                        )
                        .add(new LeftNavigationItem("Home", VaadinIcon.HOME.create(), ScheduleView.class),
                                new LeftNavigationItem("Grid", VaadinIcon.TABLE.create(), EventsView.class),
                                LeftSubMenuBuilder.get("My Submenu", VaadinIcon.PLUS.create())
                                        .add(LeftSubMenuBuilder
                                                        .get("My Submenu", VaadinIcon.PLUS.create())
                                                        .add(new LeftNavigationItem("Contact", VaadinIcon.CONNECT.create(), NewEntryView.class),
                                                                new LeftNavigationItem("More", VaadinIcon.COG.create(), NewEventView.class))
                                                        .build(),
                                                new LeftNavigationItem("Contact1", VaadinIcon.CONNECT.create(), AboutAppView.class),
                                                new LeftNavigationItem("More1", VaadinIcon.COG.create(), SettingsView.class))
                                        .build(),
                                menuEntry)
                        .addToSection(Section.FOOTER, new LeftClickableItem("Clickable Entry", VaadinIcon.COG.create(), clickEvent -> Notification.show("onClick ...")))
                        .build())
                .build());
    }

    public DefaultNotificationHolder getNotifications() {
        return notifications;
    }

    public DefaultBadgeHolder getBadge() {
        return badge;
    }
}
