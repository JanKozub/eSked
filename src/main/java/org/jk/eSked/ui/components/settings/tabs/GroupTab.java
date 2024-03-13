package org.jk.eSked.ui.components.settings.tabs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.jk.eSked.ui.components.settings.fields.GroupCodeField;
import org.jk.eSked.ui.components.settings.fields.GroupCreator;

import java.util.UUID;

public class GroupTab extends SettingsTab {
    public GroupTab(UserService userService, GroupService groupService, String title) {
        super(new Label(title));
        UUID userId = SessionService.getUserId();

        Button groupButton = new Button(getTranslation("group.leave"));
        GroupCreator groupCreator = new GroupCreator(userId, groupService, userService);

        FormLayout groupsForm = new FormLayout();
        GroupCodeField groupCodeField = new GroupCodeField(userId, userService, groupService, groupButton, groupCreator);
        groupsForm.add(groupCodeField);
        Button groupSyn = new Button(getTranslation("group.sync"));
        groupSyn.addClickListener(buttonClickEvent -> {
            if (userService.getGroupCode(userId) != 0) {
                groupService.synchronizeWGroup(userId, userService.getGroupCode(userId));
            }
        });
        groupSyn.getStyle().set("margin-top", "auto");
        groupsForm.add(groupSyn);

        RadioButtonGroup<String> eventSync = new RadioButtonGroup<>();
        eventSync.setLabel(getTranslation("group.sync.events"));
        eventSync.setItems(getTranslation("enable"), getTranslation("disable"));
        if (userService.isEventsSyn(userId)) eventSync.setValue(getTranslation("enable"));
        else eventSync.setValue(getTranslation("disable"));
        eventSync.addValueChangeListener(valueChange -> userService.setEventsSyn(userId, valueChange.getValue().equals(getTranslation("enable"))));
        groupsForm.add(eventSync);

        RadioButtonGroup<String> tableSync = new RadioButtonGroup<>();
        tableSync.setLabel(getTranslation("group.sync.schedule"));
        tableSync.setItems(getTranslation("enable"), getTranslation("disable"));
        if (userService.isTableSyn(userId)) tableSync.setValue(getTranslation("enable"));
        else tableSync.setValue(getTranslation("disable"));
        tableSync.addValueChangeListener(valueChange -> userService.setTableSyn(userId, valueChange.getValue().equals(getTranslation("enable"))));
        groupsForm.add(tableSync);

        Details newGroup = new Details(getTranslation("group.new"), groupCreator);
        newGroup.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);

        groupsForm.add(newGroup);

        groupButton.getStyle().set("color", "red");
        groupButton.setVisible(false);
        if (userService.getGroupCode(userId) != 0) {
            if (groupService.getLeaderId(userService.getGroupCode(userId)).compareTo(userId) == 0) {
                groupButton.setText(getTranslation("group.delete"));
                groupButton.addClickListener(click -> {
                    groupService.deleteGroup(userService.getGroupCode(userId));
                    new SuccessNotification(getTranslation("group.deleted"), NotificationType.SHORT).open();
                    userService.setGroupCode(userId, 0);
                    groupCreator.setMainLayout();
                    groupCodeField.updateMainValue("");
                    groupButton.setVisible(false);
                });
            } else {
                groupButton.setText(getTranslation("group.leave"));
                groupButton.addClickListener(click -> {
                    new SuccessNotification(getTranslation("group.left"), NotificationType.SHORT).open();
                    userService.setGroupCode(userId, 0);
                    groupCreator.setMainLayout();
                    groupCodeField.updateMainValue("");
                    groupButton.setVisible(false);
                });
            }
            groupButton.setVisible(true);
        }

        groupsForm.add(groupButton);
        add(groupsForm);
    }
}
