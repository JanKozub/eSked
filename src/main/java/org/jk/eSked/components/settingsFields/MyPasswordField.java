package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import org.jk.eSked.model.User;
import org.jk.eSked.services.emailService.EmailService;
import org.jk.eSked.services.users.UserService;

import javax.mail.MessagingException;
import javax.validation.ValidationException;
import java.util.Random;
import java.util.UUID;

public class MyPasswordField extends VerticalLayout {
    private Button button;
    private PasswordField passwordField;
    private Button commitButton;
    private UserService userService;
    private UUID userId;
    private EmailService emailService;

    public MyPasswordField(UUID userId, UserService userService, EmailService emailService) {
        this.userId = userId;
        this.userService = userService;
        this.emailService = emailService;
        Label label = new Label("Hasło");
        label.getStyle().set("font-size", "var(--lumo-font-size-s)");
        label.getStyle().set("font-weight", "500");
        label.getStyle().set("color", "var(--lumo-secondary-text-color)");
        add(label);

        passwordField = new PasswordField();
        passwordField.setReadOnly(true);
        passwordField.setWidth("70%");

        button = new Button("Zmień");
        button.setWidth("30%");
        button.addClickListener(this::onStartEdit);

        commitButton = new Button("Potwierdź");
        commitButton.setWidth("40%");
        commitButton.addClickListener(this::onCommit);

        HorizontalLayout buttons = new HorizontalLayout();

        buttons.add(passwordField);
        buttons.setWidth("100%");
        buttons.add(button);

        setPadding(false);
        setSpacing(false);
        add(buttons);
    }

    private void onStartEdit(ClickEvent event) {
        HorizontalLayout parent = (HorizontalLayout) button.getParent().orElse(null);
        assert parent != null;
        parent.replace(button, commitButton);
        passwordField.setWidth("60%");
        passwordField.clear();
        passwordField.setReadOnly(false);
        passwordField.setPlaceholder("Nowe hasło");
    }

    private void onCommit(ClickEvent event) {
        try {
            String pass = passwordField.getValue();
            validatePassword(pass);
            Random random = new Random();
            int code = random.nextInt(89999) + 10000;

            String emailBody = "Witaj " + userService.getUsername(userId) + "," + "<br><br>Twój kod zmiany hasła to: " + "<br><br>" + code +
                    "<br><br>" + "Teraz możesz wpisać go na stronie!" + "<br><br> Z poważaniem, <br>Zespół eSked";
            emailService.sendEmail(userService.getEmail(userId), "Potwierdzenie zmiany hasła w eSked!", emailBody);

            removeAll();

            Label label = new Label("Hasło");
            label.getStyle().set("font-size", "var(--lumo-font-size-s)");
            label.getStyle().set("font-weight", "500");
            label.getStyle().set("color", "var(--lumo-secondary-text-color)");
            add(label);

            PasswordField codeField = new PasswordField();
            codeField.setPlaceholder("Kod zmiany hasła");
            codeField.setWidth("70%");
            Button button = new Button("Potwierdź");
            button.setWidth("40%");
            HorizontalLayout buttons = new HorizontalLayout();
            buttons.add(codeField);
            buttons.setWidth("100%");
            buttons.add(button);

            setPadding(false);
            setSpacing(false);
            add(buttons);

            button.addClickListener(click -> {
                if (codeField.getValue().equals(Integer.toString(code))) {
                    System.out.println(codeField.getValue());
                    userService.changePassword(userId, User.encodePassword(pass));

                    Notification notification = new Notification("Twoje hasło zostało zmienione!", 5000, Notification.Position.TOP_END);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.open();

                    completeEdit();
                } else {
                    codeField.setErrorMessage("Podany kod jest nie prawidłowy");
                    codeField.setInvalid(true);
                }
            });
        } catch (ValidationException ex) {
            passwordField.setErrorMessage(ex.getMessage());
            passwordField.setInvalid(true);
        } catch (MessagingException mex) {

        }
    }

    private void completeEdit() {
        removeAll();

        Label label = new Label("Hasło");
        label.getStyle().set("font-size", "var(--lumo-font-size-s)");
        label.getStyle().set("font-weight", "500");
        label.getStyle().set("color", "var(--lumo-secondary-text-color)");
        add(label);

        passwordField = new PasswordField();
        passwordField.setReadOnly(true);
        passwordField.setWidth("70%");

        button = new Button("Zmień");
        button.setWidth("30%");
        button.addClickListener(this::onStartEdit);

        commitButton = new Button("Potwierdź");
        commitButton.setWidth("40%");
        commitButton.addClickListener(this::onCommit);

        HorizontalLayout buttons = new HorizontalLayout();

        buttons.add(passwordField);
        buttons.setWidth("100%");
        buttons.add(button);

        setPadding(false);
        setSpacing(false);
        add(buttons);
    }

    private void validatePassword(String password) {
        if (password.isEmpty()) throw new ValidationException("Pole nie może być puste");
    }
}
