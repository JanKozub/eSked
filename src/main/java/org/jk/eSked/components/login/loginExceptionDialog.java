package org.jk.eSked.components.login;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import org.jk.eSked.components.myImpl.SuccessNotification;
import org.jk.eSked.model.User;
import org.jk.eSked.services.emailService.EmailService;
import org.jk.eSked.services.users.UserService;

import javax.mail.MessagingException;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Random;

public class loginExceptionDialog extends Dialog {
    private final UserService userService;
    private final EmailService emailService;
    private final TextField passField;
    private final Button passButton;
    private Registration registration;
    private int counter = 0;
    private int code = 0;

    public loginExceptionDialog(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
        this.passField = new TextField();
        this.passButton = new Button("Wyślij");

        add(createMainLayout());

        registration = passButton.addClickListener(event -> {
            try {
                validateNameInput(passField.getValue());
                passField.setInvalid(false);

                String username = passField.getValue();
                sendEmail(username);

                passField.setPlaceholder("Kod z wiad. email");
                passField.clear();

                //passButton.setText("Potwierdź"); //TODO FIX
                registration = passButton.addClickListener(newEvent -> {
                    try {
                        validateCode(passField.getValue(), code);
                        passField.setInvalid(false);

                        PasswordField pass1 = new PasswordField("Nowe Hasło");
                        PasswordField pass2 = new PasswordField("Powtórz Nowe Hasło");
                        Button confirm = new Button("Zmień hasło!", event3 -> {
                            try {
                                validatePasswords(pass1.getValue(), pass2.getValue());
                                pass1.setInvalid(false);
                                pass2.setInvalid(false);
                                userService.changePassword(userService.getIdFromUsername(username), User.encodePassword(pass2.getValue()));

                                SuccessNotification notification = new SuccessNotification("Zmieniono hasło!");
                                notification.open();

                                close();
                                removeAll();
                                passButton.setText("Zmień");
                                passField.clear();
                                add(createMainLayout());

                            } catch (ValidationException ex) {
                                pass2.setErrorMessage(ex.getMessage());
                                pass1.setInvalid(true);
                                pass2.setInvalid(true);
                            }
                        });
                        confirm.addClickShortcut(Key.ENTER);
                        confirm.setWidth("100%");
                        VerticalLayout layout = new VerticalLayout(pass1, pass2, confirm);
                        removeAll();
                        add(layout);
                    } catch (ValidationException ex) {
                        passField.setErrorMessage(ex.getMessage());
                        passField.setInvalid(true);
                        try {
                            if (ex.getClass().equals(CodeException.class)) sendEmail(username);
                        } catch (MessagingException mex) {
                            passField.setErrorMessage(ex.getMessage());
                            passField.setInvalid(true);
                        }
                    }
                });
            } catch (ValidationException ex) {
                passField.setErrorMessage(ex.getMessage());
                passField.setInvalid(true);
            } catch (MessagingException mex) {
                passField.setErrorMessage("Email error contact admin " + mex.getMessage());
                passField.setInvalid(true);
            }
        });
    }

    private void sendEmail(String username) throws MessagingException {
        Random random = new Random();
        code = random.nextInt(89999) + 10000;
        String emailBody = "Witaj " + username + "," + "<br><br>Twój kod zmiany hasła to: " + "<br><br>" + code +
                "<br><br>" + "Teraz możesz wpisać go na stronie!" + "<br><br> Z poważaniem, <br>Zespół eSked";
        emailService.sendEmail(userService.getEmailFromUsername(username), "Potwierdzenie zmiany hasła w eSked!", emailBody);
        //passField.setReadOnly(false);
    }

    private VerticalLayout createMainLayout() {
        Label newUserLabel = new Label("Stwórz konto");
        newUserLabel.getStyle().set("font-weight", "bold");

        Button newUser = new Button("Utwórz", click -> {
            close();
            new NewUserDialog(userService, emailService).open();
        });
        newUser.setWidth("100%");

        Label passLabel = new Label("Odzyskaj Hasło");
        passLabel.getStyle().set("font-weight", "bold");

        passField.setPlaceholder("Nazwa Użytkownika");
        passField.setWidth("60%");

        passButton.setText("Wyślij");
        passButton.addClickShortcut(Key.ENTER);
        passButton.setWidth("40%");

        HorizontalLayout passFieldLayout = new HorizontalLayout(passField, passButton);
        passFieldLayout.setWidth("100%");

        VerticalLayout mainLayout = new VerticalLayout(newUserLabel, newUser, passLabel, passFieldLayout);
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        return mainLayout;
    }

    private void validateNameInput(String intput) {
        if (intput.isEmpty()) throw new ValidationException("Pole nie może być puste");

        Collection<String> users = userService.getUsernames();
        if (!users.contains(intput)) throw new ValidationException("Użytkownik z taką nazwą nie istnieje");
    }

    private void validatePasswords(String input1, String input2) {
        if (input1.isEmpty()) throw new ValidationException("Pola nie mogą być puste");

        if (input2.isEmpty()) throw new ValidationException("Pola nie mogą być puste");

        if (!input1.equals(input2)) throw new ValidationException("Hasła muszą być identyczne");

    }

    private void validateCode(String typedCode, int code) {
        if (typedCode.isEmpty()) throw new ValidationException("Pole nie może być puste");

        if (!typedCode.equals(Integer.toString(code))) {
            counter++;
            if (counter == 3) {
                passField.setEnabled(false);
                counter = 0;
                throw new CodeException();
            }
            throw new ValidationException("Podany kod jest nie prawidłowy");
        }
    }

    private static class CodeException extends ValidationException {
        private CodeException() {
            super("Podano nieprawidłowy kod zbyt wiele razy. Wysłano kod ponownie");
        }
    }
}