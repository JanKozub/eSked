package org.jk.eSked.ui.views.admin.users;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.*;
import org.jk.eSked.ui.components.myComponents.AdminReturnButton;
import org.jk.eSked.ui.components.myComponents.Line;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.jk.eSked.ui.components.event.EventGrid;
import org.jk.eSked.ui.components.schedule.ScheduleGrid;
import org.jk.eSked.ui.components.settings.fields.GroupCodeField;
import org.jk.eSked.ui.components.settings.fields.GroupCreator;
import org.jk.eSked.ui.components.settings.fields.NameField;
import org.jk.eSked.ui.components.settings.protectedFields.EmailField;
import org.jk.eSked.ui.components.settings.protectedFields.MyPasswordField;
import org.jk.eSked.ui.views.MainLayout;
import org.springframework.security.access.annotation.Secured;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;


@Route(value = "admin/user", layout = MainLayout.class)
@PageTitle("Sprawdź Użytkownika")
@Secured("ROLE_ADMIN")
class FindUserView extends VerticalLayout { //TODO check for code repetition
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private final UserService userService;
    private final EventService eventService;
    private final HoursService hoursService;
    private final ScheduleService scheduleService;
    private final EmailService emailService;
    private final GroupService groupService;

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

        Button button = new Button("Szukaj", event -> searchForUser(textField));
        button.addClickShortcut(Key.ENTER);
        button.setWidth("50%");

        setAlignItems(Alignment.CENTER);
        add(new AdminReturnButton(), textField, button, createAddUserLayout(userService));
    }

    private VerticalLayout createAddUserLayout(UserService userService) {
        Line line = new Line();
        Text text = new Text("Nazwa");
        TextField username = new TextField("Username");
        TextField email = new TextField("email");
        PasswordField password = new PasswordField("Haslo");

        Button addUser = new Button(getTranslation(locale, "add"), e -> {
            boolean canBeCreated = !userService.getUsernames().contains(username.getValue()) && !userService.getEmails().contains(email.getValue());

            if (canBeCreated) createUser(username.getValue(), email.getValue(), password.getValue());
            else denyUserCreation();
        });

        VerticalLayout addUserLayout = new VerticalLayout(line, text, username, email, password, addUser);
        addUserLayout.setAlignItems(Alignment.CENTER);
        return addUserLayout;
    }

    private VerticalLayout userLayout(User user) {
        InfoBox id = new InfoBox("ID: ", user.getId().toString());
        NameField username = new NameField(user.getId(), userService, emailService);
        MyPasswordField password = new MyPasswordField(user.getId(), userService, emailService, false);
        InfoBox darkTheme = new InfoBox("Dark Theme: ", Boolean.toString(user.isDarkTheme()));
        InfoBox scheduleHours = new InfoBox("Schedule hours: ", Boolean.toString(user.isScheduleHours()));
        EmailField email = new EmailField(user.getId(), userService, emailService, false);
        GroupCodeField groupCode = new GroupCodeField(user.getId(), userService, groupService, new Button(),
                new GroupCreator(SessionService.getUserId(), groupService, userService));
        InfoBox synWGroup = new InfoBox("Synchronizacja z grupą: ", Boolean.toString(user.isEventsSyn()));
        InfoBox createdDate = new InfoBox("Data stworzenia konta: ", TimeService.InstantToLocalDateTime(user.getCreatedDate()).toString());
        InfoBox lastLoggedDate = new InfoBox("Data ostatniego zalogowania: ", TimeService.InstantToLocalDateTime(user.getLastLoggedDate()).toString());

        Label scheduleLabel = new Label("Plan");
        VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, hoursService);
        Label eventsLabel = new Label("Wydarzenia");
        EventGrid eventGrid = new EventGrid(scheduleService, eventService, LocalDate.now()); //TODO check if is working

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

    private void createUser(String username, String email, String password) {
        userService.addUser(new User(UUID.randomUUID(),
                username,
                User.encodePassword(password),
                false,
                false,
                email,
                0,
                false,
                false,
                TimeService.now(),
                TimeService.now(),
                false));
        SuccessNotification successNotification = new SuccessNotification("dodano", NotificationType.SHORT);
        successNotification.open();
    }

    private void denyUserCreation() {
        Notification notification = new Notification("nie dodano", NotificationType.SHORT.getDuration());
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_END);
        notification.open();
    }

    private void searchForUser(TextField textField) {
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
    }

    private User validateInput(String input) {
        if (input.isEmpty()) throw new ValidationException(getTranslation(locale, "exception_empty_field"));

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
