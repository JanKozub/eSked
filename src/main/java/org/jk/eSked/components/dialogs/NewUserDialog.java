package org.jk.eSked.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.model.User;
import org.jk.eSked.services.users.UserService;

import java.time.Instant;
import java.util.UUID;

public class NewUserDialog extends Dialog {

    public NewUserDialog(UserService userService) {

        VerticalLayout layout = new VerticalLayout();

        Label nameOfDialog = new Label("Nowy Użytkownik");

        TextField usernameField = new TextField("Nazwa użtykownika");
        usernameField.isRequired();
        usernameField.setWidth("100%");

        EmailField emailField = new EmailField("Email");
//        emailField.isRequired();
        emailField.setWidth("100%");

        PasswordField passwordField = new PasswordField("Hasło");
        passwordField.isRequired();
        passwordField.setWidth("100%");

        PasswordField passwordFieldCheck = new PasswordField("Powtórz hasło");
        passwordFieldCheck.isRequired();
        passwordFieldCheck.setWidth("100%");

        Button addButton = new Button("Zarejstruj się!");
        addButton.setWidth("75%");
        addButton.addClickListener(e -> {
            SimplePopup popup = new SimplePopup();
            if (passwordField.getValue().equals(passwordFieldCheck.getValue()) && !passwordField.getValue().equals("")
                    && !usernameField.getValue().equals("") && !emailField.getValue().equals("") && emailField.getValue().contains("@")) {
                if (userService.getUsernames().contains(usernameField.getValue())) {
                    popup.open("Hasła nie są identyczne lub któreś z pól jest puste");
                } else {
                    UUID ID = UUID.randomUUID();
                    User user = new User(ID, usernameField.getValue(), User.encodePassword(passwordField.getValue()), false, true, emailField.getValue(), 0, false, Instant.now().toEpochMilli(), Instant.now().toEpochMilli());
                    userService.addUser(user);
                    close();
                }
            } else {
                passwordFieldCheck.clear();
                popup.open("Użytkownik istnieje");
            }
        });
        layout.add(nameOfDialog, usernameField, emailField, passwordField, passwordFieldCheck, addButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        setWidth("300px");
        add(layout);
    }
}
