package org.jk.esked.app.frontend.components.settings.fields;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.SuccessNotification;

import java.util.Collection;
import java.util.UUID;

public class GroupCreator extends VerticalLayout {
    private final UUID userId;
    private final GroupService groupsService;
    private final UserService userService;

    public GroupCreator(UUID userId, GroupService groupsService, UserService userService) {
        this.userId = userId;
        this.groupsService = groupsService;
        this.userService = userService;

        checkUserStatus();
    }

    public void setMainLayout() {
        removeAll();

        Text name = new Text(getTranslation("group.new"));
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
//                groupsService.addGroup(new Group(groupName.getValue(), new Random().nextInt(8999) + 1000, userId, false, TimeService.now()));
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

    private void validateInput(String input, UUID userId, GroupService groupsService) throws ValidationException{
        if (input.isEmpty()) throw new ValidationException(getTranslation("exception.empty.field"));

        Collection<String> groups = groupsService.getAllGroupNames();
        if (groups.contains(input)) throw new ValidationException(getTranslation("exception.group.exist"));

//        if (!groupsService.hasEntries(userId))
//            throw new ValidationException(getTranslation("exception.group.cannot.create"));
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
