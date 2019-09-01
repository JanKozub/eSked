package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.model.User;
import org.jk.eSked.services.users.UserService;

import javax.validation.ValidationException;
import java.util.UUID;

public class MyPasswordField extends VerticalLayout {
    private Button button;
    PasswordField passwordField;
    private Button commitButton;
    private UserService userService;
    private UUID userId;

    public MyPasswordField(UUID userId, UserService userService) {
        this.userId = userId;
        this.userService = userService;

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
        commitButton.setWidth("20%");
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
        HorizontalLayout parent = (HorizontalLayout) button.getParent().get();
        parent.replace(button, commitButton);

        passwordField.clear();
        passwordField.setReadOnly(false);
        passwordField.setPlaceholder("Nowe hasło");
    }

    private void onCommit(ClickEvent event) {
        try {
            if (StringUtils.isBlank(passwordField.getValue()))
                throw new ValidationException("Pole z hasłem nie może być puste");

            passwordField.setInvalid(false);
            userService.changePassword(userId, User.encodePassword(passwordField.getValue()));
            Notification notification = new Notification("Hasło zostało zmienione!", 5000, Notification.Position.TOP_END);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.open();

            completeEdit();
        } catch (ValidationException ex) {
            passwordField.setErrorMessage(ex.getMessage());
            passwordField.setInvalid(true);
        }
    }

    void completeEdit() {
        passwordField.setReadOnly(true);
        passwordField.clear();
        HorizontalLayout parent = (HorizontalLayout) commitButton.getParent().get();
        parent.replace(commitButton, button);
    }
}
