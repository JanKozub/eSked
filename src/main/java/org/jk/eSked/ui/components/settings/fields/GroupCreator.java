package org.jk.eSked.ui.components.settings.fields;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.Group;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class GroupCreator extends VerticalLayout { //TODO translate
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
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

        Label name = new Label("Nowa Grupa");
        name.getStyle().set("margin-left", "auto");
        name.getStyle().set("margin-right", "auto");
        HorizontalLayout nameLabel = new HorizontalLayout(name);
        nameLabel.setWidth("100%");

        TextField groupName = new TextField();
        groupName.setWidth("100%");

        Button confirm = new Button(getTranslation(locale, "add"));
        confirm.setWidth("100%");
        confirm.addClickListener(event -> {
            try {
                validateInput(groupName.getValue(), userId, groupsService);
                groupName.setInvalid(false);
                groupsService.addGroup(new Group(groupName.getValue(), new Random().nextInt(8999) + 1000, userId, false, TimeService.now()));
                removeAll();

                new SuccessNotification("Prośba o stworzenie grupy została wysłana!", NotificationType.SHORT).open();

                add(new Label("Aktualnie czekasz na akceptacje grupy, gdy admininstrator ją potwierdzi, zostaniesz powiadomiony."));

            } catch (ValidationException ex) {
                groupName.setErrorMessage(ex.getMessage());
                groupName.setInvalid(true);
            }
        });
        add(nameLabel, groupName, confirm);
    }

    private void validateInput(String input, UUID userId, GroupService groupsService) {
        if (input.isEmpty()) throw new ValidationException(getTranslation(locale, "exception_empty_field"));

        Collection<String> groups = groupsService.getGroupNames();
        if (groups.contains(input)) throw new ValidationException("Grupa z taką nazwą już istnieje");

        if (!groupsService.hasEntries(userId))
            throw new ValidationException("Musisz mieć wpisy w tabeli aby stworzyć grupę");
    }

    void checkUserStatus() {
        removeAll();
        if (userService.getGroupCode(userId) != 0) add(new Label("Jesteś już w grupie."));
        else if (groupsService.doesCreatedGroup(userId)) {
            add(new Label("Aktualnie czekasz na akceptacje grupy, gdy admininstrator ją potwierdzi, zostaniesz powiadomiony."));
        } else {
            setMainLayout();
        }
    }
}
