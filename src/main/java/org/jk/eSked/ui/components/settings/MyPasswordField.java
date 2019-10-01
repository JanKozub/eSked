package org.jk.eSked.ui.components.settings;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.UserService;
import org.jk.eSked.ui.components.myImpl.SuccessNotification;

import javax.validation.ValidationException;
import java.util.UUID;

public class MyPasswordField extends VerticalLayout {
    private final UserService userService;
    private final UUID userId;
    private final EmailService emailService;
    private final boolean needConfirm;
    private Button button;
    private PasswordField passwordField;
    private Button commitButton;

    public MyPasswordField(UUID userId, UserService userService, EmailService emailService, boolean needConfirm) {
        this.userId = userId;
        this.userService = userService;
        this.emailService = emailService;
        this.needConfirm = needConfirm;
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
        if (needConfirm) {
            try {
                String pass = passwordField.getValue();
                validatePassword(pass);

                emailService.sendEmail(userService.getUser(userId), EmailType.NEWPASSOWRD);
                new SuccessNotification("Link do zmiany hasła został wysłany na twój email", NotificationType.LONG);

            } catch (Exception ex) {
                passwordField.setErrorMessage(ex.getMessage());
                passwordField.setInvalid(true);
            }
        } else {
            try {
                validatePassword(passwordField.getValue());
                userService.changePassword(userId, User.encodePassword(passwordField.getValue()));

                Notification notification = new Notification("Twoje hasło zostało zmienione!", 5000, Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.open();

                completeEdit();
            } catch (ValidationException ex) {
                passwordField.setErrorMessage(ex.getMessage());
                passwordField.setInvalid(true);
            }
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
