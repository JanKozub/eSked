package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.fields.GroupCodeField;
import org.jk.esked.app.frontend.components.fields.GroupCreator;
import org.jk.esked.app.frontend.components.other.SuccessNotification;

import java.util.UUID;

@CssImport("./styles/group-tab.css")
public class GroupTab extends SettingsTab {
    private final UUID userId;

    public GroupTab(UUID userId, UserService userService, GroupService groupService, MessageService messageService) {
        super(SettingsTabType.GROUP);
        this.userId = userId;

        GroupCreator groupCreator = new GroupCreator(userId, userService, groupService);

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

        Details newGroup = new Details(getTranslation("group.new"), groupCreator);
        newGroup.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);

        Button groupButton = new Button(getTranslation("group.leave"));
        groupButton.getStyle().set("color", "red");
        groupButton.setVisible(false);
        if (userService.getGroupCodeByUserId(userId) != 0) {
            if (groupService.getLeaderIdByGroupCode(userService.getGroupCodeByUserId(userId)).compareTo(userId) == 0) {
                groupButton.setText(getTranslation("group.delete"));
                groupButton.addClickListener(click -> {
                    groupService.deleteGroupByGroupCode(userService.getGroupCodeByUserId(userId));
                    resetLayout(groupCodeField, groupButton, userService, groupCreator, getTranslation("group.deleted"));
                });
            } else {
                groupButton.setText(getTranslation("group.leave"));
                groupButton.addClickListener(click -> resetLayout(groupCodeField, groupButton, userService, groupCreator, getTranslation("group.left")));
            }
            groupButton.setVisible(true);
        }

        add(new FormLayout(groupCodeField, groupSyn, eventSync, tableSync, newGroup, groupButton));
    }

    private void resetLayout(GroupCodeField groupCodeField, Button groupButton, UserService userService, GroupCreator groupCreator, String notification) {
        new SuccessNotification(notification, NotificationType.SHORT).open();
        userService.changeGroupCodeByUserId(userId, 0);
        groupCreator.setMainLayout();
        groupCodeField.updateMainValue("");
        groupButton.setVisible(false);
    }
}
