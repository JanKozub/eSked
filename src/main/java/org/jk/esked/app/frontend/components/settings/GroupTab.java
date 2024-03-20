package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import org.jk.esked.app.backend.model.entities.Group;
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
    private final UUID userId;
    private int userGroupCode;
    private final UserService userService;
    private final GroupService groupService;
    private final Button leaveButton = new Button(getTranslation("group.leave"));
    private final Button newGroup = new Button(getTranslation("group.new"));

    public GroupTab(UUID userId, UserService userService, GroupService groupService, MessageService messageService) {
        super(SettingsTabType.GROUP);
        this.userId = userId;
        userGroupCode = userService.getGroupCodeByUserId(userId);
        this.userService = userService;
        this.groupService = groupService;
        leaveButton.addClassName("leave-button");
        leaveButton.setVisible(false);

        GroupCodeField groupCodeField = new GroupCodeField(userId, userService, groupService, messageService);
        Button groupSyn = new Button(getTranslation("group.sync"));
        groupSyn.addClickListener(buttonClickEvent ->
                groupService.synchronizeWGroup(userId, userService.getGroupCodeByUserId(userId)));

        SettingsRadioGroup eventSync = new SettingsRadioGroup("group.sync.events",
                "enable", "disable", userService.isEventsSynByUserId(userId));
        eventSync.addValueChangeListener(valueChange ->
                userService.changeEventsSynByUserId(userId, valueChange.getValue().equals(getTranslation("enable"))));

        SettingsRadioGroup tableSync = new SettingsRadioGroup("group.sync.schedule",
                "enable", "disable", userService.isTableSyn(userId));
        tableSync.addValueChangeListener(valueChange ->
                userService.changeTableSynByUserId(userId, valueChange.getValue().equals(getTranslation("enable"))));

        newGroup.addClickListener(e -> createNewGroup(groupCodeField));

        if (userGroupCode != 0) {
            newGroup.setEnabled(false);

            if (groupService.getGroupByGroupCode(userGroupCode).isAccepted()) {
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
        if (userGroupCode != 0) return;

        Group group = new Group();
        group.setLeader(userService.getUserById(userId));
        group.setGroupCode(getUniqueGroupCode());
        groupService.saveGroup(group);

        userService.changeGroupCodeByUserId(userId, group.getGroupCode());
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
        UUID leaderId = groupService.getLeaderIdByGroupCode(userGroupCode);
        if (leaderId.compareTo(userId) == 0) {
            leaveButton.setText(getTranslation("group.delete"));
            leaveButton.addClickListener(click -> {
                groupService.deleteGroupByGroupCode(userService.getGroupCodeByUserId(userId));
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
        userService.changeGroupCodeByUserId(userId, 0);
        groupCodeField.updateMainValue("");
        groupButton.setVisible(false);
        newGroup.setEnabled(true);
        userGroupCode = 0;
    }
}
