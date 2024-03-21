package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import org.jk.esked.app.backend.model.entities.Group;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.fields.GroupCodeField;
import org.jk.esked.app.frontend.components.other.SuccessNotification;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@CssImport("./styles/group-tab.css")
public class GroupTab extends SettingsTab {
    private final UserService userService;
    private final GroupService groupService;
    private final Button leaveButton = new Button(getTranslation("group.leave"));
    private final Button newGroup = new Button(getTranslation("group.new"));
    private final User user;

    public GroupTab(UUID userId, UserService userService, GroupService groupService, MessageService messageService) {
        super(SettingsTabType.GROUP);
        this.user = userService.findById(userId);
        this.userService = userService;
        this.groupService = groupService;
        leaveButton.addClassName("leave-button");
        leaveButton.setVisible(false);

        GroupCodeField groupCodeField = new GroupCodeField(user, userService, groupService, messageService);
        Button groupSyn = new Button(getTranslation("group.sync"));
        groupSyn.addClickListener(buttonClickEvent ->
                groupService.synchronizeWGroup(userId, user.getGroupCode()));

        SettingsRadioGroup eventSync = new SettingsRadioGroup("group.sync.events",
                "enable", "disable", user.isEventSync());
        eventSync.addValueChangeListener(valueChange ->
                userService.changeEventsSynById(userId, valueChange.getValue().equals(getTranslation("enable"))));

        SettingsRadioGroup tableSync = new SettingsRadioGroup("group.sync.schedule",
                "enable", "disable", user.isTableSync());
        tableSync.addValueChangeListener(valueChange ->
                userService.changeTableSynById(userId, valueChange.getValue().equals(getTranslation("enable"))));

        newGroup.addClickListener(e -> createNewGroup(groupCodeField));

        if (user.getGroupCode() != 0) {
            newGroup.setEnabled(false);

            if (groupService.getGroupByGroupCode(user.getGroupCode()).isAccepted()) {
                showLeaveButton(groupCodeField);
            } else {
                leaveButton.setText(getTranslation("group.pending"));
                leaveButton.setVisible(true);
                leaveButton.setEnabled(false);
            }
        }

        add(new FormLayout(groupCodeField, groupSyn, eventSync, tableSync, newGroup, leaveButton));
    }

    private void createNewGroup(GroupCodeField groupCodeField) {
        if (user.getGroupCode() != 0) return;

        Group group = new Group();
        group.setLeader(user);
        group.setGroupCode(getUniqueGroupCode());
        groupService.saveGroup(group);

        userService.changeGroupCodeById(user.getId(), group.getGroupCode());
        groupCodeField.updateMainValue(String.valueOf(group.getGroupCode()));
        newGroup.setEnabled(false);
        leaveButton.setText(getTranslation("group.pending"));
        leaveButton.setVisible(true);
    }

    private int getUniqueGroupCode() {
        int code = new Random().nextInt(899999) + 100000;
        List<Integer> codes = groupService.getAllGroupCodes();

        while (codes.contains(code))
            code = new Random().nextInt(899999) + 100000;

        return code;
    }

    private void showLeaveButton(GroupCodeField groupCodeField) {
        UUID leaderId = groupService.getLeaderIdByGroupCode(user.getGroupCode());
        if (leaderId.compareTo(user.getId()) == 0) {
            leaveButton.setText(getTranslation("group.delete"));
            leaveButton.addClickListener(click -> {
                groupService.deleteGroupByGroupCode(user.getGroupCode());
                resetLayout(groupCodeField, leaveButton, getTranslation("group.deleted"));
            });
        } else {
            leaveButton.setText(getTranslation("group.leave"));
            leaveButton.addClickListener(click ->
                    resetLayout(groupCodeField, leaveButton, getTranslation("group.left")));
        }
        leaveButton.setVisible(true);
    }

    private void resetLayout(GroupCodeField groupCodeField, Button groupButton, String notification) {
        new SuccessNotification(notification, NotificationType.SHORT).open();
        userService.changeGroupCodeById(user.getId(), 0);
        groupCodeField.updateMainValue("");
        groupButton.setVisible(false);
        newGroup.setEnabled(true);
        user.setGroupCode(0);
    }
}
