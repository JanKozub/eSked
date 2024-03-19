package org.jk.esked.app.frontend.components.settings.tabs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.SuccessNotification;
import org.jk.esked.app.frontend.components.settings.fields.GroupCodeField;
import org.jk.esked.app.frontend.components.settings.fields.GroupCreator;

import java.util.UUID;

public class GroupTab extends SettingsTab {
    private final UUID userId;

    public GroupTab(UUID userId, UserService userService, GroupService groupService, ScheduleService scheduleService, MessageService messageService) {
        super(SettingsTabType.GROUP);
        this.userId = userId;

        Button groupButton = new Button(getTranslation("group.leave"));
        GroupCreator groupCreator = new GroupCreator(userId, userService, groupService, scheduleService);

        GroupCodeField groupCodeField = new GroupCodeField(userId, userService, groupService, messageService);
        Button groupSyn = new Button(getTranslation("group.sync"));
        groupSyn.addClickListener(buttonClickEvent -> {
            if (userService.getGroupCodeByUserId(userId) != 0)
                groupService.synchronizeWGroup(userId, userService.getGroupCodeByUserId(userId));
        });
        groupSyn.getStyle().set("margin-top", "auto");

        RadioButtonGroup<String> eventSync = new RadioButtonGroup<>();
        eventSync.setLabel(getTranslation("group.sync.events"));
        eventSync.setItems(getTranslation("enable"), getTranslation("disable"));
        if (userService.isEventsSynByUserId(userId)) eventSync.setValue(getTranslation("enable"));
        else eventSync.setValue(getTranslation("disable"));
        eventSync.addValueChangeListener(valueChange -> userService.changeEventsSynByUserId(userId, valueChange.getValue().equals(getTranslation("enable"))));

        RadioButtonGroup<String> tableSync = new RadioButtonGroup<>();
        tableSync.setLabel(getTranslation("group.sync.schedule"));
        tableSync.setItems(getTranslation("enable"), getTranslation("disable"));
        if (userService.isTableSyn(userId)) tableSync.setValue(getTranslation("enable"));
        else tableSync.setValue(getTranslation("disable"));
        tableSync.addValueChangeListener(valueChange -> userService.changeTableSynByUserId(userId, valueChange.getValue().equals(getTranslation("enable"))));

        Details newGroup = new Details(getTranslation("group.new"), groupCreator);
        newGroup.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);

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