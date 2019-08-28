package org.jk.eSked.view.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.view.MenuView;

@Route(value = "settings", layout = MenuView.class)
@PageTitle("Ustawienia")
public class SettingsView extends VerticalLayout {

    private User user = VaadinSession.getCurrent().getAttribute(User.class);

    public SettingsView(LoginService loginService, UserService userService, GroupsService groupsService, ScheduleService scheduleService) {
        if (loginService.checkIfUserIsLogged()) {
            setSizeFull();
            setAlignItems(Alignment.CENTER);
//ACCOUNT
            Label accountLabel = new Label("Użytkownik");

            FormLayout accountForm = new FormLayout();
            accountForm.add(new TextFieldInput("Nazwa", userService.getUserName(user.getId())));
            accountForm.add(new PasswordFieldInput("Hasło", "dupa"));
            accountForm.add(new TextFieldInput("Email", userService.getEmail(user.getId())));
            RadioButtonGroup<String> languageGroup = new RadioButtonGroup<>();
            languageGroup.setLabel("Język");
            languageGroup.setItems("Polski", "English");
            accountForm.add(languageGroup);
//GROUPS
            Label groupsLabel = new Label("Grupy");

            FormLayout groupsForm = new FormLayout();
            groupsForm.add(new TextFieldInput("Kod", Integer.toString(userService.getGroupCode(user.getId()))));
            RadioButtonGroup<String> autoSync = new RadioButtonGroup<>();
            autoSync.setLabel("Automatyczna Synchronizacja");
            autoSync.setItems("Włącz", "Wyłącz");
            groupsForm.add(autoSync);
            groupsForm.add(new Button("Synchronizuj z grupą"));
            groupsForm.add(new Button("Nowa grupa"));
//SCHEDULE
            Label other = new Label("Inne");

            RadioButtonGroup<String> scheduleHours = new RadioButtonGroup<>();
            scheduleHours.setLabel("godziny w planie");
            scheduleHours.setItems("Tak", "Nie");

            RadioButtonGroup<String> theme = new RadioButtonGroup<>();
            theme.setLabel("Styl strony");
            theme.setItems("Jasny", "Ciemny");

            HorizontalLayout layout = new HorizontalLayout(scheduleHours, theme);
            layout.setAlignItems(Alignment.CENTER);

            VerticalLayout verticalLayout = new VerticalLayout(accountLabel, new Line(), accountForm, groupsLabel, new Line(), groupsForm, other, new Line(), layout);
            verticalLayout.setWidth("50%");
            verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, layout);

            add(verticalLayout);
        }
    }

    private static class Line extends Div {
        private Line() {
            getStyle().set("background-color", "#d8e1ed");
            setWidth("100%");
            setHeight("3px");
        }
    }
}