package org.jk.esked.app.frontend.views.other;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.backend.services.utilities.EmailService;
import org.jk.esked.app.frontend.components.settings.AccountTab;
import org.jk.esked.app.frontend.components.settings.GroupTab;
import org.jk.esked.app.frontend.components.settings.OtherTab;
import org.jk.esked.app.frontend.views.MainLayout;
import org.springframework.context.annotation.Scope;

import java.util.UUID;

@SpringComponent
@PermitAll
@Scope("prototype")
@Route(value = "settings", layout = MainLayout.class)
@CssImport("./styles/settings.css")
public class SettingsView extends VerticalLayout implements HasDynamicTitle {
    public SettingsView(SecurityService securityService, UserService userService, GroupService groupService, EmailService emailService, MessageService messageService) {
        UUID userId = securityService.getUserId();
        add(
                new AccountTab(userId, userService, emailService),
                new GroupTab(userId, userService, groupService, messageService),
                new OtherTab(userId, userService, securityService)
        );
        addClassName("settings-view");
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.settings");
    }
}
