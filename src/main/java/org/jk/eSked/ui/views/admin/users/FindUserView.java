package org.jk.eSked.ui.views.admin.users;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.*;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.myImpl.AdminReturnButton;
import org.jk.eSked.ui.components.schedule.EventGrid;
import org.jk.eSked.ui.components.schedule.ScheduleGrid;
import org.jk.eSked.ui.components.settings.fields.GroupCodeField;
import org.jk.eSked.ui.components.settings.fields.NameField;
import org.jk.eSked.ui.components.settings.protectedFields.EmailField;
import org.jk.eSked.ui.components.settings.protectedFields.MyPasswordField;

import javax.validation.ValidationException;
import java.time.Instant;
import java.time.ZoneId;


@Route(value = "admin/user", layout = MenuView.class)
@PageTitle("Sprawdź Użytkownika")
class FindUserView extends VerticalLayout {

    private final UserService userService;
    private final EventService eventService;
    private final HoursService hoursService;
    private final ScheduleService scheduleService;
    private final EmailService emailService;
    private GroupService groupService;

    FindUserView(ScheduleService scheduleService, UserService userService, EventService eventService, HoursService hoursService, EmailService emailService, GroupService groupService) {
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.eventService = eventService;
        this.hoursService = hoursService;
        this.emailService = emailService;
        this.groupService = groupService;
        SessionService.setAutoTheme();

        TextField textField = new TextField("Nazwa użytkownika");
        textField.setWidth("50%");
        textField.focus();

        Button button = new Button("Szukaj", event -> {
            try {
                User user = validateInput(textField.getValue());
                textField.setInvalid(false);
                removeAll();

                add(userLayout(user));
                setAlignItems(Alignment.CENTER);
            } catch (ValidationException ex) {
                textField.setErrorMessage(ex.getMessage());
                textField.setInvalid(true);
            }
        });
        button.addClickShortcut(Key.ENTER);
        button.setWidth("50%");

        setAlignItems(Alignment.CENTER);
        add(new AdminReturnButton(), textField, button);
    }

    private VerticalLayout userLayout(User user) {
        InfoBox id = new InfoBox("ID: ", user.getId().toString());
        NameField username = new NameField(user.getId(), userService, emailService);
        MyPasswordField password = new MyPasswordField(user.getId(), userService, emailService, false);
        InfoBox darkTheme = new InfoBox("Dark Theme: ", Boolean.toString(user.isDarkTheme()));
        InfoBox scheduleHours = new InfoBox("Schedule hours: ", Boolean.toString(user.isScheduleHours()));
        EmailField email = new EmailField(user.getId(), userService, emailService, false);
        GroupCodeField groupCode = new GroupCodeField(user.getId(), userService, groupService);
        InfoBox synWGroup = new InfoBox("Synchronizacja z grupą: ", Boolean.toString(user.isEventsSyn()));
        InfoBox createdDate = new InfoBox("Data stworzenia konta: ", Instant.ofEpochMilli(user.getLastLoggedDate()).atZone(ZoneId.systemDefault()).toLocalDateTime().toString());
        InfoBox lastLoggedDate = new InfoBox("Data ostatniego zalogowania: ", Instant.ofEpochMilli(user.getLastLoggedDate()).atZone(ZoneId.systemDefault()).toLocalDateTime().toString());

        Label scheduleLabel = new Label("Plan");
        VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, hoursService, user.getId());
        Label eventsLabel = new Label("Wydarzenia");
        VerticalLayout eventGrid = new EventGrid(scheduleService, eventService, user.getId());

        Button deleteButton = new Button("Usuń Konto", e -> {
            userService.deleteUser(user.getId());
            UI.getCurrent().navigate("admin");
        });
        deleteButton.getStyle().set("color", "red");
        deleteButton.setWidth("100%");

        VerticalLayout layout = new VerticalLayout(id, username, password, email, groupCode, darkTheme, scheduleHours,
                synWGroup, createdDate, lastLoggedDate, scheduleLabel, scheduleGrid, eventsLabel, eventGrid, deleteButton);
        layout.setAlignItems(Alignment.CENTER);
        return layout;
    }


    private User validateInput(String input) {
        if (input.isEmpty()) throw new ValidationException("Pole nie może być puste");

        User user = findUser(input);
        if (user == null) throw new ValidationException("Użytkownik nie istnieje");
        else return user;
    }

    private User findUser(String username) {
        for (User user : userService.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private static class InfoBox extends HorizontalLayout {
        private InfoBox(String name, String data) {
            TextField textField = new TextField();
            textField.setValue(name + data);
            textField.setReadOnly(true);
            textField.setWidth("100%");

            add(textField);
            setVerticalComponentAlignment(Alignment.CENTER, textField);
            setWidth("100%");
        }
    }
}