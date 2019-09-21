package org.jk.eSked.components.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.components.myImpl.SuccessNotification;
import org.jk.eSked.model.User;
import org.jk.eSked.services.emailService.EmailService;
import org.jk.eSked.services.users.UserService;

import javax.mail.MessagingException;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

public class EmailField extends SettingsTextField {

    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;
    private final boolean needConfirm;

    public EmailField(UUID userId, UserService userService, EmailService emailService, boolean needConfirm) {
        super("Email", "Nowy Email");
        this.userId = userId;
        this.userService = userService;
        this.emailService = emailService;
        this.needConfirm = needConfirm;
        textField.setValue(userService.getEmail(userId));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z emailem nie może być puste");

        Collection<String> emails = userService.getEmails();
        if (emails.contains(textField.getValue())) {
            throw new ValidationException("Taki email jest juz zarejstrowyny");
        }

        if (!input.contains("@")) {
            throw new ValidationException("Podany tekst nie jest emailem");
        }
    }

    @Override
    protected void commitInput(String input) throws MessagingException {
        TextField codeField = new TextField();
        Button button = new Button("Potwierdź");
        button.setWidth("40%");

        if (needConfirm) {
            Random random = new Random();
            int code = random.nextInt(89999) + 10000;

            String emailBody = "Witaj " + userService.getUsername(VaadinSession.getCurrent().getAttribute(User.class).getId()) +
                    "," + "<br><br>Twój kod zmiany emailu to: " + "<br><br>" + code +
                    "<br><br>" + "Teraz możesz wpisać go na stronie!" + "<br><br> Z poważaniem, <br>Zespół eSked";
            emailService.sendEmail(input, "Potwierdzenie zmiany emailu w eSked!", emailBody);

            removeAll();
            codeField.setPlaceholder("Kod z email");
            codeField.setWidth("60%");
            codeField.getStyle().set("margin-top", "0px");

            button.addClickListener(click -> {
                if (codeField.getValue().equals(Integer.toString(code))) {
                    userService.changeEmail(userId, textField.getValue());

                    SuccessNotification notification = new SuccessNotification("Zmieniono email na \"" + textField.getValue() + "\"");
                    notification.open();

                    completeEdit(userService.getEmail(userId));
                } else {
                    codeField.setErrorMessage("Podany kod jest nie prawidłowy");
                    codeField.setInvalid(true);
                }
            });
            Label label = new Label("Email");
            label.getStyle().set("font-size", "var(--lumo-font-size-s)");
            label.getStyle().set("font-weight", "500");
            label.getStyle().set("color", "var(--lumo-secondary-text-color)");
            label.getStyle().set("margin-top", "0px");
            HorizontalLayout layout = new HorizontalLayout(codeField, button);
            layout.setPadding(false);
            layout.getStyle().set("margin-top", "0px");
            VerticalLayout verticalLayout = new VerticalLayout(label, layout);
            verticalLayout.setPadding(false);
            layout.setWidth("100%");
            verticalLayout.setWidth("100%");
            add(verticalLayout);
        } else {
            userService.changeEmail(userId, textField.getValue());

            SuccessNotification notification = new SuccessNotification("Zmieniono email na \"" + textField.getValue() + "\"");
            notification.open();

            completeEdit(userService.getEmail(userId));
        }
    }
}