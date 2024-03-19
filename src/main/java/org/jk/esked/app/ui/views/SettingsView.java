package org.jk.esked.app.ui.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.*;
import org.jk.esked.app.ui.components.settings.tabs.AccountTab;
import org.jk.esked.app.ui.components.settings.tabs.DeleteTab;
import org.springframework.context.annotation.Scope;

import java.util.UUID;

@SpringComponent
@PermitAll
@Scope("prototype")
@Route(value = "settings", layout = MainLayout.class)
public class SettingsView extends VerticalLayout implements HasDynamicTitle {
    public SettingsView(SecurityService securityService, UserService userService, GroupService groupService, EmailService emailService, HourService hoursService, MessageService messageService) {
        UUID userId = securityService.getUserId();
        add(
                new AccountTab(userId, userService, emailService, getTranslation("user")),
//                new GroupTab(userId, userService, groupService, messageService, getTranslation("group")),
//                new OtherTab(userId, userService, hoursService, getTranslation("other")),
                new DeleteTab(userId, userService, getTranslation("settings.tab.delete.acc"))
        );
        setSizeFull();
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.settings");
    }
}
