package org.jk.eSked.ui.components.settings.protectedFields;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.UserService;

import javax.validation.ValidationException;
import java.util.Locale;

abstract class ProtectedSettingsField extends VerticalLayout {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private final String baseName;
    private final String editName;
    TextField textField;
    private final UserService userService;
    private final String value;
    private final TextField confirmTextField = new TextField();
    private final PasswordField confirmPasswordField = new PasswordField();
    private PasswordField passwordField;
    private Button button;
    private Registration registration;

    ProtectedSettingsField(UserService userService, String label, String value, String editName) {
        this.userService = userService;
        this.baseName = label;
        this.value = value;
        this.editName = editName;

        setMainLayout(value);
    }

    private void onStartEdit() {
        button.setText("Potwierdź");
        button.setWidth("40%");

        remove(textField.getParent().orElse(null), button.getParent().orElse(null));
        registration = button.addClickListener(b -> onInput());
        HorizontalLayout layout = new HorizontalLayout(passwordField, button);
        layout.setWidth("100%");
        add(layout);
    }

    private void onInput() {
        try {
            validatePassword(passwordField.getValue());

            remove(passwordField.getParent().orElse(null), button.getParent().orElse(null));
            registration.remove();
            registration = button.addClickListener(b -> onCommit());

            HorizontalLayout layout = new HorizontalLayout();
            if (value.isEmpty()) {
                confirmPasswordField.setWidth("60%");
                confirmPasswordField.setPlaceholder(editName);

                layout.add(confirmPasswordField, button);
            } else {
                confirmTextField.setWidth("60%");
                confirmTextField.setPlaceholder(editName);

                layout.add(confirmTextField, button);
            }
            layout.setWidth("100%");
            add(layout);
        } catch (ValidationException ex) {
            passwordField.setErrorMessage(ex.getMessage());
            passwordField.setInvalid(true);
            passwordField.clear();
        }
    }

    private void onCommit() {
        if (value.isEmpty()) {
            try {
                validateInput(confirmPasswordField.getValue());
                confirmPasswordField.setInvalid(false);
                commitInput(confirmPasswordField.getValue());
            } catch (Exception ex) {
                confirmPasswordField.setErrorMessage(ex.getMessage());
                confirmPasswordField.setInvalid(true);
            }
        } else {
            try {
                validateInput(confirmTextField.getValue());
                confirmTextField.setInvalid(false);
                commitInput(confirmTextField.getValue());
            } catch (Exception ex) {
                confirmTextField.setErrorMessage(ex.getMessage());
                confirmTextField.setInvalid(true);
            }
        }
    }

    void setMainLayout(String data) {
        removeAll();

        Label label = new Label(baseName);
        label.getStyle().set("font-size", "var(--lumo-font-size-s)");
        label.getStyle().set("font-weight", "500");
        label.getStyle().set("color", "var(--lumo-secondary-text-color)");
        add(label);

        textField = new TextField();
        textField.setReadOnly(true);
        textField.setValue(data);
        textField.setWidth("70%");

        passwordField = new PasswordField();
        passwordField.setWidth("70%");
        passwordField.setPlaceholder("Wpisz hasło");

        button = new Button("Zmień");
        button.setWidth("30%");
        button.addClickListener(b -> onStartEdit());

        HorizontalLayout buttons = new HorizontalLayout();

        buttons.add(textField);
        buttons.setWidth("100%");
        buttons.add(button);

        setPadding(false);
        setSpacing(false);
        add(buttons);
    }

    private void validatePassword(String password) {
        if (password.isEmpty()) throw new ValidationException(getTranslation(locale, "exception_empty_field"));

        if (!userService.getUser(SessionService.getUserId())
                .getPassword().equals(User.encodePassword(password)))
            throw new ValidationException("Nieprawidłowe hasło");
    }

    protected abstract void validateInput(String input);

    protected abstract void commitInput(String input) throws Exception;
}