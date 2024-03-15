package org.jk.eSked.ui.components.settings.fields;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.validation.ValidationException;
import org.jk.eSked.backend.model.Group;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

public class GroupCreator extends VerticalLayout {
    private final UUID userId;
    private final GroupService groupsService;
    private final UserService userService;

    public GroupCreator(GroupService groupsService, UserService userService) {
        this.userId = SessionService.getUserId();
        this.groupsService = groupsService;
        this.userService = userService;

        checkUserStatus();
    }

    public void setMainLayout() {
        removeAll();

        Label name = new Label(getTranslation("group.new"));
        name.getStyle().set("margin-left", "auto");
        name.getStyle().set("margin-right", "auto");
        HorizontalLayout nameLabel = new HorizontalLayout(name);
        nameLabel.setWidth("100%");

        TextField groupName = new TextField();
        groupName.setWidth("100%");

        Button confirm = new Button(getTranslation("add"));
        confirm.setWidth("100%");
        confirm.addClickListener(event -> {
            try {
                validateInput(groupName.getValue(), userId, groupsService);
                groupName.setInvalid(false);
                groupsService.addGroup(new Group(groupName.getValue(), new Random().nextInt(8999) + 1000, userId, false, TimeService.now()));
                removeAll();

                new SuccessNotification(getTranslation("notification.group.sent"), NotificationType.SHORT).open();

                add(new Label(getTranslation("group.pending")));
            } catch (ValidationException ex) {
                groupName.setErrorMessage(ex.getMessage());
                groupName.setInvalid(true);
            }
        });
        add(nameLabel, groupName, confirm);
    }

    private void validateInput(String input, UUID userId, GroupService groupsService) {
        if (input.isEmpty()) throw new ValidationException(getTranslation("exception.empty.field"));

        Collection<String> groups = groupsService.getGroupNames();
        if (groups.contains(input)) throw new ValidationException(getTranslation("exception.group.exist"));

        if (!groupsService.hasEntries(userId))
            throw new ValidationException(getTranslation("exception.group.cannot.create"));
    }

    void checkUserStatus() {
        removeAll();
        if (userService.getGroupCode(userId) != 0) {
            add(new Label(getTranslation("group.member")));
            return;
        }

        if (groupsService.doesCreatedGroup(userId)) {
            add(new Label(getTranslation("group.pending")));
            return;
        }

        setMainLayout();
    }
}
