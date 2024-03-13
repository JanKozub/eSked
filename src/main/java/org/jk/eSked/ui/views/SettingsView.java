package org.jk.eSked.ui.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.HoursService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.settings.tabs.AccountTab;
import org.jk.eSked.ui.components.settings.tabs.DeleteTab;
import org.jk.eSked.ui.components.settings.tabs.GroupTab;
import org.jk.eSked.ui.components.settings.tabs.OtherTab;
import java.util.Locale;

@Route(value = "settings", layout = MainLayout.class)
public class SettingsView extends VerticalLayout implements HasDynamicTitle {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();

    public SettingsView(UserService userService, GroupService groupService, EmailService emailService, HoursService hoursService) {
        SessionService.setAutoTheme();

        add(
                new AccountTab(userService, emailService, getTranslation(locale, "user"), locale),
                new GroupTab(userService, groupService, getTranslation(locale, "group"), locale),
                new OtherTab(userService, hoursService, getTranslation(locale, "other"), locale),
                new DeleteTab(userService, getTranslation(locale, "settings_tab_delete_acc"), locale)
        );
        setSizeFull();
    }

    @Override
    public String getPageTitle() {
        return getTranslation(locale, "page_settings");
    }
}
