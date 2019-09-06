package org.jk.eSked.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.components.SuccessNotification;
import org.jk.eSked.model.User;
import org.jk.eSked.services.emailService.EmailService;
import org.jk.eSked.services.users.UserService;

import javax.mail.MessagingException;
import javax.validation.ValidationException;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

public class NewUserDialog extends Dialog {
    private final UserService userService;

    public NewUserDialog(UserService userService, EmailService emailService) {
        this.userService = userService;

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
            validateUsername(usernameField.getValue());
            try {
                usernameField.setInvalid(false);
                try {
                    validateEmail(emailField.getValue());
                    emailField.setInvalid(false);
                    try {
                        validatePassword(passwordField.getValue(), passwordFieldCheck.getValue());
                        passwordField.setInvalid(false);
                        passwordFieldCheck.setInvalid(false);

                        int genCode = new Random().nextInt(89999) + 10000;
                        User user = new User(UUID.randomUUID(), usernameField.getValue(), User.encodePassword(passwordField.getValue()), false, true, emailField.getValue(), 0, false, Instant.now().toEpochMilli(), Instant.now().toEpochMilli(), genCode);

                        String emailBody = "Witaj " + usernameField.getValue() + "," +
                                "<br><br>Dziękujemy za zarejstrowanie się na naszej stronie, oto twój kod weryfikacji: " +
                                "<br><br>" + genCode + "<br><br> Z poważaniem, <br>Zespół eSked";
                        emailService.sendEmail("Potwierdzenie rejstracji w eSked!", usernameField.getValue(), emailBody);

                        removeAll();

                        Label label = new Label("Przepisz kod wysłany na podany email");

                        TextField codeField = new TextField();
                        codeField.setPlaceholder("Kod");

                        Button confirm = new Button("Potwierdź");
                        confirm.addClickListener(event -> {
                            if (codeField.getValue().equals(Integer.toString(genCode))) {
                                codeField.setInvalid(false);
                                userService.addUser(user);

                                SuccessNotification notification = new SuccessNotification("Konto zostało poprawnie stworzone!");
                                notification.open();

                                close();
                            } else {
                                codeField.setErrorMessage("Podany kod jest nie prawidłowy");
                                codeField.setInvalid(true);
                            }
                        });
                        add(label, codeField, confirm);
                    } catch (ValidationException ex) {
                        passwordFieldCheck.setErrorMessage(ex.getMessage());
                        passwordField.setInvalid(true);
                        passwordFieldCheck.setInvalid(true);
                    } catch (MessagingException mex) {
                        passwordFieldCheck.setErrorMessage("Email error contact admin");
                        passwordFieldCheck.setInvalid(true);
                    }
                } catch (ValidationException ex) {
                    emailField.setErrorMessage(ex.getMessage());
                    emailField.setInvalid(true);
                }
            } catch (ValidationException ex) {
                usernameField.setErrorMessage(ex.getMessage());
                usernameField.setInvalid(true);
            }
        });
        layout.add(nameOfDialog, usernameField, emailField, passwordField, passwordFieldCheck, addButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        setWidth("300px");
        add(layout);
    }

    private void validateUsername(String username) {
        if (username.isEmpty()) throw new ValidationException("Pole nie może być puste");

        Collection<String> usernames = userService.getUsernames();
        if (usernames.contains(username)) throw new ValidationException("Użytkownik z taką nazwą już istnieje");
    }

    private void validateEmail(String email) {
        if (email.isEmpty()) throw new ValidationException("Pole nie może być puste");

        Collection<String> emails = userService.getEmails();
        if (emails.contains(email)) throw new ValidationException("Taki email jest już przypisany do innego konta");
    }

    private void validatePassword(String pass1, String pass2) {
        if (pass1.isEmpty()) throw new ValidationException("Żadne z pól nie może być puste");

        if (!pass1.equals(pass2)) throw new ValidationException("Hasła nie są identyczne");
    }
}