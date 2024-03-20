package org.jk.esked.app.frontend.components.fields;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.esked.app.backend.model.entities.Group;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.other.SuccessNotification;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

public class GroupCreator extends VerticalLayout {
    private final UUID userId;
    private final GroupService groupsService;
    private final UserService userService;

    public GroupCreator(UUID userId, UserService userService, GroupService groupsService) {
        this.userId = userId;
        this.groupsService = groupsService;
        this.userService = userService;

        checkUserStatus();
    }

    public void setMainLayout() {
        removeAll();

        Span name = new Span(getTranslation("group.new"));
        name.addClassName("centered-text");

        HorizontalLayout nameLabel = new HorizontalLayout(name);
        nameLabel.setWidth("100%");

        TextField groupName = new TextField();
        groupName.setWidth("100%");

        Button confirm = new Button(getTranslation("add"));
        confirm.setWidth("100%");
        confirm.addClickListener(event -> {
            try {
                validateInput(groupName.getValue());
                groupName.setInvalid(false);

                Group group = new Group();
                group.setLeader(userService.getUserById(userId));
                group.setName(groupName.getValue());
                group.setGroupCode(new Random().nextInt(8999) + 1000);
                groupsService.saveGroup(group);

                removeAll();

                new SuccessNotification(getTranslation("notification.group.sent"), NotificationType.SHORT).open();

                add(new Text(getTranslation("group.pending")));
            } catch (ValidationException ex) {
                groupName.setErrorMessage(ex.getMessage());
                groupName.setInvalid(true);
            }
        });
        add(nameLabel, groupName, confirm);
    }

    private void validateInput(String input) throws ValidationException {
        if (input.isEmpty()) throw new ValidationException(getTranslation("exception.empty.field"));

        Collection<String> groups = groupsService.getAllGroupNames();
        if (groups.contains(input)) throw new ValidationException(getTranslation("exception.group.exist"));
    }

    void checkUserStatus() {
        removeAll();
        if (userService.getGroupCodeByUserId(userId) != 0) {
            add(new Text(getTranslation("group.member")));
            return;
        }

        if (groupsService.isGroupCreatedByUser(userId)) {
            add(new Text(getTranslation("group.pending")));
            return;
        }

        setMainLayout();
    }
}
