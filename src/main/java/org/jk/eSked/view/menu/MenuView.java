package org.jk.eSked.view.menu;

import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.entity.Section;
import com.github.appreciated.app.layout.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.notification.component.AppBarNotificationButton;
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.component.CheckTimeTheme;
import org.jk.eSked.model.User;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.view.AboutAppView;
import org.jk.eSked.view.admin.AdminView;
import org.jk.eSked.view.events.EventsView;
import org.jk.eSked.view.events.NewEventView;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.schedule.NewEntryView;
import org.jk.eSked.view.schedule.ScheduleView;
import org.jk.eSked.view.settings.SettingsView;

import java.util.logging.Level;
import java.util.logging.Logger;

@Push
public class MenuView extends AppLayoutRouterLayout {

    private static final Logger LOGGER = Logger.getLogger(MenuView.class.getName());
    private final UserService userService;
    @SuppressWarnings("FieldCanBeLocal")
    private final DefaultNotificationHolder notifications;

    public MenuView(LoginService loginService, UserService userService) {
        this.userService = userService;
        notifications = new DefaultNotificationHolder(newStatus -> {

        });
        //DefaultBadgeHolder badge = new DefaultBadgeHolder(5);

        if (loginService.checkIfUserIsLoggedAsAdmin()) {
            init(AppLayoutBuilder
                    .get(Behaviour.LEFT_RESPONSIVE_HYBRID)
                    .withTitle("Plan Lekcji")
                    .withAppBar(AppBarBuilder.get()
                            .add(new AppBarNotificationButton<>(VaadinIcon.BELL, notifications))
                            .build())
                    .withAppMenu(LeftAppMenuBuilder.get()
                            .add(new LeftNavigationItem("Plan", VaadinIcon.CALENDAR_O.create(), ScheduleView.class))
                            .add(new LeftNavigationItem("Wydarzenia", VaadinIcon.CALENDAR_CLOCK.create(), EventsView.class))
                            .add(LeftSubMenuBuilder.get("Dodaj", VaadinIcon.PLUS_CIRCLE_O.create())
                                    .add(new LeftNavigationItem("Wydarzenie", VaadinIcon.FOLDER_ADD.create(), NewEventView.class))
                                    .add(new LeftNavigationItem("Do planu lub Edytuj", VaadinIcon.EXTERNAL_BROWSER.create(), NewEntryView.class))
                                    .build())
                            .add(new LeftNavigationItem("Ustawienia", VaadinIcon.COG_O.create(), SettingsView.class))
                            .add(new LeftNavigationItem("O stronie", VaadinIcon.INFO_CIRCLE_O.create(), AboutAppView.class))
                            .add(new LeftNavigationItem("Admininstracja", VaadinIcon.CALC_BOOK.create(), AdminView.class))
                            .addToSection(new LeftClickableItem("Wyloguj się", VaadinIcon.POWER_OFF.create(), e -> logout()), Section.FOOTER)
                            .build())
                    .build());
        } else {
            init(AppLayoutBuilder
                    .get(Behaviour.LEFT_RESPONSIVE_HYBRID)
                    .withTitle("Plan Lekcji")
                    .withAppBar(AppBarBuilder.get()
                            .add(new AppBarNotificationButton<>(VaadinIcon.BELL, notifications))
                            .build())
                    .withAppMenu(LeftAppMenuBuilder.get()
                            .add(new LeftNavigationItem("Plan", VaadinIcon.CALENDAR_O.create(), ScheduleView.class))
                            .add(new LeftNavigationItem("Wydarzenia", VaadinIcon.CALENDAR_CLOCK.create(), EventsView.class))
                            .add(LeftSubMenuBuilder.get("Dodaj", VaadinIcon.PLUS_CIRCLE_O.create())
                                    .add(new LeftNavigationItem("Wydarzenie", VaadinIcon.FOLDER_ADD.create(), NewEventView.class))
                                    .add(new LeftNavigationItem("Do planu lub Edytuj", VaadinIcon.EXTERNAL_BROWSER.create(), NewEntryView.class))
                                    .build())
                            .add(new LeftNavigationItem("Ustawienia", VaadinIcon.COG_O.create(), SettingsView.class))
                            .add(new LeftNavigationItem("O stronie", VaadinIcon.INFO_CIRCLE_O.create(), AboutAppView.class))
                            .addToSection(new LeftClickableItem("Wyloguj się", VaadinIcon.POWER_OFF.create(), e -> logout()), Section.FOOTER)
                            .build())
                    .build());
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        try {
            if (userService.getDarkTheme(VaadinSession.getCurrent().getAttribute(User.class).getId())) {
                UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"dark\")");
            }
        } catch (NullPointerException ex) {
            LOGGER.log(Level.INFO, "User with ip: " + UI.getCurrent().getSession().getBrowser().getAddress() + " tried to use schedule without logging");
        }
    }

    private void logout() {
        VaadinSession.getCurrent().close();
        CheckTimeTheme checkTimeTheme = new CheckTimeTheme();
        checkTimeTheme.check();
        UI.getCurrent().navigate("login");
    }

//    public void newNotification(String topic, String description) {
//        notifications.addNotification(new DefaultNotification(topic, description));
//    }
}
