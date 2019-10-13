package org.jk.eSked.ui.components.settings.tabs;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.eSked.backend.ApplicationContextHolder;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.HoursService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.Line;

import java.util.UUID;

abstract class SettingsTab extends VerticalLayout {

    UserService userService;
    EmailService emailService;
    GroupService groupService;
    HoursService hoursService;
    UUID userId;

    SettingsTab(Label label) {
        this.userService = ApplicationContextHolder.getContext().getBean(UserService.class);
        this.emailService = ApplicationContextHolder.getContext().getBean(EmailService.class);
        this.groupService = ApplicationContextHolder.getContext().getBean(GroupService.class);
        this.hoursService = ApplicationContextHolder.getContext().getBean(HoursService.class);
        this.userId = SessionService.getUserId();

        add(label, new Line());
        getStyle().set("flex-shrink", "0");
        getStyle().set("padding-top", "8px");
    }
}
