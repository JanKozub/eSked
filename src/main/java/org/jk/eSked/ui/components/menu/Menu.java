package org.jk.eSked.ui.components.menu;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Component
@UIScope
public class Menu extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> {
    private static final Logger log = LoggerFactory.getLogger(Menu.class);
    private DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private DefaultBadgeHolder badge = new DefaultBadgeHolder(5);

    public Menu() {
        notifications.addClickListener(notification -> {
        });
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
        } else log.error("@secured annotation is equal null");
        menuBar.addItem(VaadinIcon.POWER_OFF.create(), e -> logout());

        LeftNavigationItem[] tab = tabs();

        init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
                .withTitle("eSked")
                .withAppBar(AppBarBuilder.get()
                        .add(menuBar)
                        .build())
                .withAppMenu(LeftAppMenuBuilder.get()
                        .add(tab[0], tab[1], tab[2])
                        .build())
                .build());
    }

    private LeftNavigationItem[] tabs() {
        LeftNavigationItem schedule = new LeftNavigationItem("Plan", VaadinIcon.CALENDAR_O.create(), ScheduleView.class);
        LeftNavigationItem events = new LeftNavigationItem("Wydarzenia", VaadinIcon.CALENDAR_CLOCK.create(), EventsView.class);
        badge.bind(events.getBadge());
        LeftNavigationItem newEvent = new LeftNavigationItem("Dodaj Wydarzenie", VaadinIcon.FOLDER_ADD.create(), NewEventView.class);
        return new LeftNavigationItem[]{schedule, events, newEvent};
    }

    public DefaultNotificationHolder getNotifications() {
        return notifications;
    }

    public DefaultBadgeHolder getBadge() {
        return badge;
    }

    private void logout() {
        SecurityContextHolder.clearContext();
        UI.getCurrent().navigate("login");
        VaadinSession.getCurrent().close();
    }
}
