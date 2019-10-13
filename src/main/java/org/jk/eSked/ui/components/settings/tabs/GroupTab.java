package org.jk.eSked.ui.components.settings.tabs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.jk.eSked.ui.components.settings.fields.GroupCodeField;
import org.jk.eSked.ui.components.settings.fields.GroupCreator;

public class GroupTab extends SettingsTab {

    public GroupTab() {
        super(new Label("Grupy"));

        Button groupButton = new Button("Wyjdź z grupy");
        GroupCreator groupCreator = new GroupCreator(userId, groupService, userService);

        FormLayout groupsForm = new FormLayout();
        GroupCodeField groupCodeField = new GroupCodeField(userId, userService, groupService, groupButton, groupCreator);
        groupsForm.add(groupCodeField);
        Button groupSyn = new Button("Synchronizuj z grupą");
        groupSyn.addClickListener(buttonClickEvent -> {
            if (userService.getGroupCode(userId) != 0) {
                groupService.synchronizeWGroup(userId, userService.getGroupCode(userId));
            }
        });
        groupSyn.getStyle().set("margin-top", "auto");
        groupsForm.add(groupSyn);

        RadioButtonGroup<String> eventSync = new RadioButtonGroup<>();
        eventSync.setLabel("Synchronizacja z Wydarzeniami");
        eventSync.setItems("Włącz", "Wyłącz");
        if (userService.isEventsSyn(userId)) eventSync.setValue("Włącz");
        else eventSync.setValue("Wyłącz");
        eventSync.addValueChangeListener(valueChange -> userService.setEventsSyn(userId, valueChange.getValue().equals("Włącz")));
        groupsForm.add(eventSync);

        RadioButtonGroup<String> tableSync = new RadioButtonGroup<>();
        tableSync.setLabel("Synchronizacja z Tabelą");
        tableSync.setItems("Włącz", "Wyłącz");
        if (userService.isTableSyn(userId)) tableSync.setValue("Włącz");
        else tableSync.setValue("Wyłącz");
        tableSync.addValueChangeListener(valueChange -> userService.setTableSyn(userId, valueChange.getValue().equals("Włącz")));
        groupsForm.add(tableSync);

        Details newGroup = new Details("Nowa Grupa", groupCreator);
        newGroup.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);

        groupsForm.add(newGroup);

        groupButton.getStyle().set("color", "red");
        groupButton.setVisible(false);
        if (userService.getGroupCode(userId) != 0) {
            if (groupService.getLeaderId(userService.getGroupCode(userId)).compareTo(userId) == 0) {
                groupButton.setText("Usuń grupę");
                groupButton.addClickListener(click -> {
                    groupService.deleteGroup(userService.getGroupCode(userId));
                    new SuccessNotification("Usunięto grupę", NotificationType.SHORT).open();
                    userService.setGroupCode(userId, 0);
                    groupCreator.setMainLayout();
                    groupCodeField.clear();
                    groupButton.setVisible(false);
                });
            } else {
                groupButton.setText("Wyjdź z grupy");
                groupButton.addClickListener(click -> {
                    new SuccessNotification("Opuszczono grupę", NotificationType.SHORT).open();
                    userService.setGroupCode(userId, 0);
                    groupCreator.setMainLayout();
                    groupCodeField.clear();
                    groupButton.setVisible(false);
                });
            }
            groupButton.setVisible(true);
        }

        groupsForm.add(groupButton);
        add(groupsForm);
    }
}
