package org.jk.eSked.ui.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.HoursService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.settings.tabs.AccountTab;
import org.jk.eSked.ui.components.settings.tabs.DeleteTab;
import org.jk.eSked.ui.components.settings.tabs.GroupTab;
import org.jk.eSked.ui.components.settings.tabs.OtherTab;

@Route(value = "settings", layout = MainLayout.class)
public class SettingsView extends VerticalLayout implements HasDynamicTitle {
    public SettingsView(UserService userService, GroupService groupService, EmailService emailService, HoursService hoursService) {
        SessionService.setAutoTheme();

        add(
                new AccountTab(userService, emailService, getTranslation("user")),
                new GroupTab(userService, groupService, getTranslation("group")),
                new OtherTab(userService, hoursService, getTranslation("other")),
                new DeleteTab(userService, getTranslation("settings.tab.delete.acc"))
        );
        setSizeFull();
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.settings");
    }
}
