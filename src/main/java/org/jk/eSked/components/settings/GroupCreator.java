package org.jk.eSked.components.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.components.myImpl.SuccessNotification;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.users.UserService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

public class GroupCreator extends VerticalLayout {
    public GroupCreator(UUID userId, GroupsService groupsService, UserService userService) {

        if (userService.getGroupCode(userId) != 0) add(new Label("Jesteś już w grupie."));
        else if (groupsService.doesCreatedGroup(userId)) {
            add(new Label("Aktualnie czekasz na akceptacje grupy, gdy admininstrator ją potwierdzi, zostaniesz powiadomiony."));
        } else {
            Label name = new Label("Nowa Grupa");
            name.getStyle().set("margin-left", "auto");
            name.getStyle().set("margin-right", "auto");
            HorizontalLayout nameLabel = new HorizontalLayout(name);
            nameLabel.setWidth("100%");

            TextField groupName = new TextField();
            groupName.setWidth("100%");

            Button confirm = new Button("Dodaj");
            confirm.setWidth("100%");
            confirm.addClickListener(event -> {
                try {
                    validateInput(groupName.getValue(), userId, groupsService);
                    groupName.setInvalid(false);
                    groupsService.addGroup(userId, groupName.getValue(), new Random().nextInt(8999) + 1000, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());

                    removeAll();

                    SuccessNotification notification = new SuccessNotification("Prośba o stworzenie grupy została wysłana!");
                    notification.open();

                    add(new Label("Aktualnie czekasz na akceptacje grupy, gdy admininstrator ją potwierdzi, zostaniesz powiadomiony."));

                } catch (ValidationException ex) {
                    groupName.setErrorMessage(ex.getMessage());
                    groupName.setInvalid(true);
                }
            });
            add(nameLabel, groupName, confirm);
        }
    }

    private void validateInput(String input, UUID userId, GroupsService groupsService) {
        if (input.isEmpty()) throw new ValidationException("Pole nie może być puste");

        Collection<String> groups = groupsService.getGroupsNames();
        if (groups.contains(input)) throw new ValidationException("Grupa z taką nazwą już istnieje");

        if (!groupsService.hasEntiries(userId))
            throw new ValidationException("Musisz mieć wpisy w tabeli aby stworzyć grupę");
    }
}