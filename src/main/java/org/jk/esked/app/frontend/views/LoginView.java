package org.jk.esked.app.frontend.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.jk.esked.app.backend.services.EmailService;
import org.jk.esked.app.frontend.components.login.LoginExceptionDialog;
import org.jk.esked.app.backend.services.EncryptionService;
import org.jk.esked.app.backend.services.UserService;

@Route("login")
@PageTitle("Login | eSked")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginOverlay login = new LoginOverlay(createI18n());

    public LoginView(UserService userService, EmailService emailService, EncryptionService encryptionService) {
        login.setTitle(createTitle());
        login.setDescription("");
        login.setAction("login");
        login.setOpened(true);
        login.addForgotPasswordListener(f -> {
            Dialog dialog = new LoginExceptionDialog(userService, emailService, encryptionService);
            dialog.open();
        });

        add(login);
    }

    private H1 createTitle() {
        H1 title = new H1();
        title.getStyle().set("color", "var(--lumo-base-color)");
        Icon icon = VaadinIcon.CALENDAR.create();
        icon.setSize("30px");
        icon.getStyle().set("top", "-4px");
        title.add(icon);
        title.add(new Text(" eSked"));

        return title;
    }

    private LoginI18n createI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form form = i18n.getForm();

        form.setTitle(getTranslation("login.title"));
        form.setUsername(getTranslation("login.username"));
        form.setPassword(getTranslation("password"));
        form.setSubmit(getTranslation("login.submit"));
        form.setForgotPassword(getTranslation("login.forgot.password"));
        i18n.getErrorMessage().setTitle(getTranslation("login.error.title"));
        i18n.getErrorMessage().setMessage(getTranslation("login.error.message"));
        i18n.setAdditionalInformation(getTranslation("login.information"));

        return i18n;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}