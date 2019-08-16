package org.jk.eSked.view.admin.users;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.components.EventGrid;
import org.jk.eSked.components.dialogs.SimplePopup;
import org.jk.eSked.components.schedule.ScheduleGrid;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.TimeService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;

class UserFoundView extends VerticalLayout {
    private final int PASS = 0;
    private final int NAME = 1;
    private final int EMAIL = 2;

    private final UserService userService;
    private TextField username;
    private TextField password;
    private TextField email;

    UserFoundView(LoginService loginService, ScheduleService scheduleService, EventService eventService, UserService userService, GroupsService groupsService, TimeService timeService, User user) {
        this.userService = userService;

        if (loginService.checkIfUserIsLogged()) {
            if (loginService.checkIfUserIsLoggedAsAdmin()) {

                Label mainLabel = new Label("Dane");

                username = new TextField();
                username.setValue("Nazwa: " + user.getUsername());
                username.setEnabled(false);
                username.setWidth("80%");
                Button usernameEdit = new Button("Zmień", e -> changeDialog(user, NAME).open());
                usernameEdit.setWidth("20%");
                HorizontalLayout usernameLaytout = new HorizontalLayout(username, usernameEdit);
                usernameLaytout.setVerticalComponentAlignment(Alignment.CENTER, username, usernameEdit);
                usernameLaytout.setWidth("50%");

                password = new TextField();
                password.setValue("Hasło: " + user.getPassword());
                password.setEnabled(false);
                password.setWidth("80%");
                Button passwordEdit = new Button("Zmień", e -> changeDialog(user, PASS).open());
                passwordEdit.setWidth("20%");
                HorizontalLayout passwordLayout = new HorizontalLayout(password, passwordEdit);
                passwordLayout.setVerticalComponentAlignment(Alignment.CENTER, password, passwordEdit);
                passwordLayout.setWidth("50%");

                email = new TextField();
                email.setValue("Email: " + user.getEmail());
                email.setEnabled(false);
                email.setWidth("80%");
                Button emailEdit = new Button("Zmień", e -> changeDialog(user, EMAIL).open());
                emailEdit.setWidth("20%");
                HorizontalLayout emailLayout = new HorizontalLayout(email, emailEdit);
                emailLayout.setVerticalComponentAlignment(Alignment.CENTER, email, emailEdit);
                emailLayout.setWidth("50%");

                InfoBox idLayout = new InfoBox("ID Użytkownika: ", user.getId().toString());

                InfoBox groupCodeLayout = new InfoBox("Kod grupy: ", Integer.toString(user.getGroupCode()));

                InfoBox scheduleHoursLayout = new InfoBox("Godziny w planie:", Boolean.toString(user.isScheduleHours()));

                InfoBox lastLoggedLayout = new InfoBox("Ostatnio zalogowany: ", user.getLastLoggedDate().toString());

                InfoBox accountCreatedAtLayout = new InfoBox("Konto utwożone dnia: ", user.getCreatedDate().toString());

                Label scheduleLabel = new Label("Plan");

                VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, groupsService, timeService, user.getId());

                Label eventsLabel = new Label("Wydarzenia");

                VerticalLayout eventGrid = new EventGrid(scheduleService, eventService, timeService, user.getId());

                Label deleteLabel = new Label("Usuń konto");
                Button deleteButton = new Button("Usuń", e -> {
                    userService.deleteUser(user.getId());
                    UI.getCurrent().navigate("admin");
                });
                deleteButton.getStyle().set("color", "red");
                deleteButton.setWidth("50%");

                setAlignItems(Alignment.CENTER);
                add(mainLabel, usernameLaytout, passwordLayout, emailLayout, idLayout, groupCodeLayout, scheduleHoursLayout, lastLoggedLayout, accountCreatedAtLayout, scheduleLabel, scheduleGrid, eventsLabel, eventGrid, deleteLabel, deleteButton);
            }
        }
    }

    private Dialog changeDialog(User user, int version) {
        Dialog dialog = new Dialog();
        TextField newText = new TextField();
        TextField confirmText = new TextField();
        Button confirmBt = new Button("Potwierdź");
        SimplePopup simplePopup = new SimplePopup();
        switch (version) {
            case 0:
                newText.setPlaceholder("Nowe hasło");
                confirmText.setPlaceholder("Potwierdź hasło");
                confirmBt.addClickListener(event -> {
                    if (newText.getValue() != null && !newText.getValue().equals("") && newText.getValue().equals(confirmText.getValue())) {
                        userService.changePassword(user.getId(), User.encodePassword(newText.getValue()));
                        password.setValue("Hasło: " + User.encodePassword(newText.getValue()));
                        dialog.close();
                    } else simplePopup.open("Wartości pól nie są identyczne lub pole jest puste");
                });
                break;
            case 1:
                newText.setPlaceholder("Nowa nazwa");
                confirmText.setPlaceholder("Potwierdź nazwe");
                confirmBt.addClickListener(event -> {
                    if (newText.getValue() != null && !newText.getValue().equals("") && newText.getValue().equals(confirmText.getValue())) {
                        userService.changeUsername(user.getId(), newText.getValue());
                        username.setValue("Nazwa: " + newText.getValue());
                        dialog.close();
                    } else simplePopup.open("Wartości pól nie są identyczne lub pole jest puste");
                });
                break;
            case 2:
                newText.setPlaceholder("Nowy email");
                confirmText.setPlaceholder("Potwierdź email");
                confirmBt.addClickListener(event -> {
                    if (newText.getValue() != null && !newText.getValue().equals("") && newText.getValue().equals(confirmText.getValue()) && newText.getValue().contains("@")) {
                        userService.changeEmail(user.getId(), newText.getValue());
                        email.setValue("Email: " + newText.getValue());
                        dialog.close();
                    } else simplePopup.open("Wartości pól nie są identyczne lub pole jest puste");
                });
                break;
        }
        VerticalLayout dialogLayout = new VerticalLayout(newText, confirmText, confirmBt);
        dialog.add(dialogLayout);
        return dialog;
    }

    private static class InfoBox extends HorizontalLayout {
        private InfoBox(String name, String data) {
            TextField id = new TextField();
            id.setValue(name + data);
            id.setEnabled(false);
            id.setWidth("100%");
            add(id);
            setVerticalComponentAlignment(Alignment.CENTER, id);
            setWidth("50%");
        }
    }
}