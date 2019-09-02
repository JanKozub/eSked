package org.jk.eSked.components.dialogs;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.model.User;
import org.jk.eSked.services.emailService.EmailService;
import org.jk.eSked.services.users.UserService;

import javax.mail.MessagingException;
import java.util.Collection;
import java.util.Random;

public class ProblemDialog extends Dialog {

    public ProblemDialog(UserService userService, EmailService emailService) {

        Icon passIcon = new Icon(VaadinIcon.CLOSE_CIRCLE);
        Label passLabel = new Label("Odzyskaj Hasło");
        passLabel.getStyle().set("font-weight", "bold");
        TextField passField = new TextField();
        passField.setPlaceholder("Nazwa Użytkownika");
        Button passButton = new Button("Wyślij");
        passButton.addClickShortcut(Key.ENTER);

        HorizontalLayout passLabelLayout = new HorizontalLayout(passIcon, passLabel);
        HorizontalLayout passFieldLayout = new HorizontalLayout(passField, passButton);
        passFieldLayout.setWidth("100%");

        Icon emailIcon = new Icon(VaadinIcon.ENVELOPE);
        Label emailLabel = new Label("Kontakt");
        emailLabel.getStyle().set("font-weight", "bold");
        Label email = new Label("eskedinfo@gmail.com");
        email.getStyle().set("font-weight", "bold");
        HorizontalLayout emailLayout = new HorizontalLayout(emailIcon, emailLabel);

        VerticalLayout mainLayout = new VerticalLayout(passLabelLayout, passFieldLayout, emailLayout, email);
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(mainLayout);

        passButton.addClickListener(event -> {
            if (!passField.isEmpty()) {
                passField.setInvalid(false);
                Collection<String> users = userService.getUsernames();
                if (users.contains(passField.getValue())) {
                    passField.setInvalid(false);
                    Random random = new Random();
                    int code = random.nextInt(89999) + 10000;
                    try {
                        emailService.sendForgotPasswordEmail(userService.getEmailFromUsername(passField.getValue()), passField.getValue(), code);
                    } catch (MessagingException ex) {

                    }
                    TextField codeField = new TextField();
                    codeField.setPlaceholder("Kod z wiad. email");
                    codeField.setWidth("100%");

                    Button button = new Button("Potwierdź", newEvent -> {
                        if (!codeField.isEmpty()) {
                            codeField.setInvalid(false);
                            if (codeField.getValue().equals(Integer.toString(code))) {
                                codeField.setInvalid(false);
                                PasswordField pass1 = new PasswordField("Nowe Hasło");

                                PasswordField pass2 = new PasswordField("Powtórz Nowe Hasło");

                                Button confirm = new Button("Zmień hasło!", event3 -> {
                                    if (!pass1.isEmpty()) {
                                        pass1.setInvalid(false);
                                        if (!pass2.isEmpty()) {
                                            pass2.setInvalid(false);
                                            if (pass1.getValue().equals(pass2.getValue())) {
                                                pass1.setInvalid(false);
                                                userService.changePassword(userService.getIdFromUsername(passField.getValue()), User.encodePassword(pass1.getValue()));

                                                Notification notification = new Notification("Zmieniono hasło", 5000, Notification.Position.TOP_END);
                                                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                                                notification.open();

                                                close();
                                                removeAll();
                                                passField.clear();
                                                add(mainLayout);
                                            } else {
                                                pass1.setErrorMessage("Hasła nie są identyczne");
                                                pass1.setInvalid(true);
                                            }
                                        } else {
                                            pass2.setErrorMessage("Pole nie może być puste");
                                            pass2.setInvalid(true);
                                        }
                                    } else {
                                        pass1.setErrorMessage("Pole nie może być puste");
                                        pass1.setInvalid(true);
                                    }
                                });
                                confirm.addClickShortcut(Key.ENTER);
                                confirm.setWidth("100%");
                                VerticalLayout layout = new VerticalLayout(pass1, pass2, confirm);
                                removeAll();
                                add(layout);
                            } else {
                                codeField.setErrorMessage("Podany kod jest nie prawidłowy");
                                codeField.setInvalid(true);
                            }
                        } else {
                            codeField.setErrorMessage("Pole nie może być puste");
                            codeField.setInvalid(true);
                        }
                    });
                    button.addClickShortcut(Key.ENTER);
                    button.setWidth("100%");
                    removeAll();
                    add(codeField, button);
                } else {
                    passField.setErrorMessage("Użytkownik nie istnieje");
                    passField.setInvalid(true);
                }
            } else {
                passField.setErrorMessage("Pole nie może być puste");
                passField.setInvalid(true);
            }
        });
    }
}