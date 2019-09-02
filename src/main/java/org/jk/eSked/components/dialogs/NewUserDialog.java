package org.jk.eSked.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.model.User;
import org.jk.eSked.services.emailService.EmailService;
import org.jk.eSked.services.users.UserService;

import javax.mail.MessagingException;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

public class NewUserDialog extends Dialog {

    public NewUserDialog(UserService userService, EmailService emailService) {
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
                                    Random random = new Random();
                                    int genCode = random.nextInt(89999) + 10000;
                                    User user = new User(ID, usernameField.getValue(), User.encodePassword(passwordField.getValue()), false, true, emailField.getValue(), 0, false, Instant.now().toEpochMilli(), Instant.now().toEpochMilli(), genCode);
                                    try {
                                        emailService.sendNewUserEmail(emailField.getValue(), usernameField.getValue(), genCode);
                                    } catch (MessagingException ex) {

                                    }
                                    removeAll();

                                    Label label = new Label("Przepisz kod wysłany na podany email");

                                    TextField codeField = new TextField();
                                    codeField.setPlaceholder("Kod");

                                    Button confirm = new Button("Potwierdź");
                                    confirm.addClickListener(event -> {
                                        if (codeField.getValue().equals(Integer.toString(genCode))) {
                                            codeField.setInvalid(false);
                                            userService.addUser(user);

                                            Notification notification = new Notification("Konto zostało poprawnie stworzone!", 5000, Notification.Position.TOP_END);
                                            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                                            notification.open();

                                            close();
                                        } else {
                                            codeField.setErrorMessage("Podany kod jest nie prawidłowy");
                                            codeField.setInvalid(true);
                                        }
                                    });
                                    add(label, codeField, confirm);
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
