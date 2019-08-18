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
import java.util.Collection;
import java.util.UUID;

public class NewUserDialog extends Dialog {

    public NewUserDialog(UserService userService) {
        Collection<String> usernames = userService.getUsernames();
        Collection<String> emails = userService.getEmails();

        VerticalLayout layout = new VerticalLayout();

        Label nameOfDialog = new Label("Nowy Użytkownik");

        TextField usernameField = new TextField("Nazwa użtykownika");
        usernameField.setWidth("100%");

        EmailField emailField = new EmailField("Email");
        emailField.setWidth("100%");

        PasswordField passwordField = new PasswordField("Hasło");
        passwordField.setWidth("100%");

        PasswordField passwordFieldCheck = new PasswordField("Powtórz hasło");
        passwordFieldCheck.setWidth("100%");

        Button addButton = new Button("Zarejstruj się!");
        addButton.setWidth("75%");
        addButton.addClickListener(e -> {
            if (!usernameField.getValue().equals("")) {
                usernameField.setInvalid(false);
                if (!usernames.contains(usernameField.getValue())) {
                    usernameField.setInvalid(false);
                    if (!emailField.getValue().equals("")) {
                        emailField.setInvalid(false);
                        if (!emails.contains(emailField.getValue())) {
                            emailField.setInvalid(false);
                            if (!passwordField.getValue().equals("")) {
                                passwordField.setInvalid(false);
                                if (passwordField.getValue().equals(passwordFieldCheck.getValue())) {
                                    passwordField.setInvalid(false);
                                    UUID ID = UUID.randomUUID();
                                    User user = new User(ID, usernameField.getValue(), User.encodePassword(passwordField.getValue()), false, true, emailField.getValue(), 0, false, Instant.now().toEpochMilli(), Instant.now().toEpochMilli());
                                    userService.addUser(user);
                                    close();
                                } else {
                                    passwordField.setErrorMessage("Hasła nie są identyczne");
                                    passwordField.setInvalid(true);
                                }
                            } else {
                                passwordField.setErrorMessage("Pole z hasłem nie może być puste");
                                passwordField.setInvalid(true);
                            }
                        } else {
                            emailField.setErrorMessage("Email jest już przypisany do innego konta");
                            emailField.setInvalid(true);
                        }
                    } else {
                        emailField.setErrorMessage("Pole z email'em musi być wypełnione");
                        emailField.setInvalid(true);
                    }
                } else {
                    usernameField.setErrorMessage("Istnieje już użytkownik z taką nazwą");
                    usernameField.setInvalid(true);
                }
            } else {
                usernameField.setErrorMessage("Pole z nazwą użytkownika nie może być puste");
                usernameField.setInvalid(true);
            }
        });
        layout.add(nameOfDialog, usernameField, emailField, passwordField, passwordFieldCheck, addButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        setWidth("300px");
        add(layout);
    }
}
