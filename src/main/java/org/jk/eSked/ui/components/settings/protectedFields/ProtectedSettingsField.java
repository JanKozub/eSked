package org.jk.eSked.ui.components.settings.protectedFields;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.UserService;

import javax.validation.ValidationException;

abstract class ProtectedSettingsField extends VerticalLayout {
    private final String baseName;
    private final String editName;
    TextField textField;
    private UserService userService;
    private String value;
    private TextField confirmTextField = new TextField();
    private PasswordField confirmPasswordField = new PasswordField();
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

    private void onStartEdit(ClickEvent event) {
        button.setText("Potwierdź");
        button.setWidth("40%");

        remove(textField.getParent().orElse(null), button.getParent().orElse(null));
        registration = button.addClickListener(this::onInput);
        HorizontalLayout layout = new HorizontalLayout(passwordField, button);
        layout.setWidth("100%");
        add(layout);
    }

    private void onInput(ClickEvent event) {
        try {
            validatePassword(passwordField.getValue());

            remove(passwordField.getParent().orElse(null), button.getParent().orElse(null));
            registration.remove();
            registration = button.addClickListener(this::onCommit);

            HorizontalLayout layout = new HorizontalLayout();
            if (value.equals("")) {
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

    private void onCommit(ClickEvent event) {
        if (value.equals("")) {
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
        button.addClickListener(this::onStartEdit);

        HorizontalLayout buttons = new HorizontalLayout();

        buttons.add(textField);
        buttons.setWidth("100%");
        buttons.add(button);

        setPadding(false);
        setSpacing(false);
        add(buttons);
    }

    private void validatePassword(String password) {
        if (password.isEmpty()) throw new ValidationException("Pole nie może być puste");

        if (!userService.getUser(VaadinSession.getCurrent()
                .getAttribute(User.class).getId())
                .getPassword().equals(User.encodePassword(password)))
            throw new ValidationException("Nieprawidłowe hasło");
    }

    protected abstract void validateInput(String input);

    protected abstract void commitInput(String input) throws Exception;
}