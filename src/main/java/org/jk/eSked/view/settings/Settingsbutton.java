package org.jk.eSked.view.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.model.User;
import org.jk.eSked.services.users.UserService;

import java.util.Collection;

public class Settingsbutton extends VerticalLayout {
    private UserService userService;
    private TextField textField;
    private Button button;
    private User user = VaadinSession.getCurrent().getAttribute(User.class);

    public Settingsbutton(UserService userService) {
        this.userService = userService;
    }

    public Settingsbutton(String name, String data, String buttonText, SettingsEntryType entryType) {
        Label title = new Label(name);

        textField = new TextField();
        textField.setValue(data);
        textField.setWidth("50%");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        textField.setEnabled(false);

        button = new Button(buttonText);
        switch (entryType) {
            case USERNAME:
                button.addClickListener(buttonClickEvent -> onClickName());
                break;
        }
        button.setWidth("50%");

        setAlignItems(Alignment.CENTER);
        add(title, textField, button);
    }

    private void onClickName() {
        textField.setEnabled(true);
        textField.setPlaceholder("Nowa nazwa");
        textField.setErrorMessage("Pole nie może być puste");
        button.setText("Potwierdź");
        button.addClickListener(buttonClickEvent -> {
            Collection<String> usernames = userService.getUsernames();
            if (!textField.getValue().equals("") && !textField.getValue().equals(user.getUsername()) && !usernames.contains(textField.getValue())) {
                textField.setInvalid(false);
                userService.changeUsername(user.getId(), textField.getValue());
            } else textField.setInvalid(true);
        });
    }
}
