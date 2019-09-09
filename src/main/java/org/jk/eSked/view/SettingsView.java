package org.jk.eSked.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.components.myImpl.Line;
import org.jk.eSked.components.myImpl.SuccessNotification;
import org.jk.eSked.components.settings.*;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.emailService.EmailService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.hours.HoursService;
import org.jk.eSked.services.users.UserService;

import java.util.UUID;

@Route(value = "settings", layout = MenuView.class)
@PageTitle("Ustawienia")
class SettingsView extends VerticalLayout {

    public SettingsView(LoginService loginService, UserService userService, GroupsService groupsService, HoursService hoursService, EmailService emailService) {

        if (loginService.checkIfUserIsLogged()) {

            UUID userId = VaadinSession.getCurrent().getAttribute(User.class).getId();

            setSizeFull();
            setAlignItems(Alignment.CENTER);
//ACCOUNT
            Label accountLabel = new Label("Użytkownik");

            FormLayout accountForm = new FormLayout();
            accountForm.add(new NameField(userId, userService, emailService));
            accountForm.add(new MyPasswordField(userId, userService, emailService));
            accountForm.add(new EmailField(userId, userService, emailService));
            RadioButtonGroup<String> languageGroup = new RadioButtonGroup<>();
            languageGroup.setLabel("Język");
            languageGroup.setItems("Polski", "English");
            languageGroup.setValue("Polski");
            languageGroup.getStyle().set("padding-top", "0px");
            languageGroup.getStyle().set("margin-top", "auto");

            accountForm.add(languageGroup);
//GROUPS
            Label groupsLabel = new Label("Grupy");

            FormLayout groupsForm = new FormLayout();
            groupsForm.add(new GroupCodeField(userId, userService));
            Button groupSyn = new Button("Synchronizuj z grupą");
            groupSyn.addClickListener(buttonClickEvent -> groupsService.synchronizeWGroup(userId, userService.getGroupCode(userId)));
            groupSyn.getStyle().set("margin-top", "auto");
            groupsForm.add(groupSyn);
            RadioButtonGroup<String> autoSync = new RadioButtonGroup<>();
            autoSync.setLabel("Automatyczna Synchronizacja");
            autoSync.setItems("Włącz", "Wyłącz");
            if (userService.isSynWGroup(userId)) autoSync.setValue("Włącz");
            else autoSync.setValue("Wyłącz");
            autoSync.addValueChangeListener(valueChange -> userService.setSynWGroup(userId, valueChange.getValue().equals("Włącz")));
            groupsForm.add(autoSync);

            Details newGroup = new Details("Nowa Grupa", new GroupCreator(userId, groupsService, userService));
            newGroup.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);

            groupsForm.add(newGroup);

            Button groupButton = new Button();
            groupButton.setWidth("100%");
            groupButton.getStyle().set("color", "red");
            groupButton.setVisible(false);
//SCHEDULE
            Label other = new Label("Inne");

            RadioButtonGroup<String> scheduleHours = new RadioButtonGroup<>();
            scheduleHours.setLabel("godziny w planie");
            scheduleHours.setItems("Tak", "Nie");
            if (userService.getScheduleHours(userId)) scheduleHours.setValue("Tak");
            else scheduleHours.setValue("Nie");
            scheduleHours.addValueChangeListener(valueChange -> userService.setScheduleHours(userId, valueChange.getValue().equals("Tak")));

            Details setHours = new Details("Ustaw godziny", new ScheduleHoursSetter(userId, hoursService));
            setHours.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
            if (hoursService.getScheduleMaxHour(userId) == 0) setHours.setEnabled(false);

            RadioButtonGroup<String> theme = new RadioButtonGroup<>();
            theme.setLabel("Styl strony");
            theme.setItems("Jasny", "Ciemny");
            if (userService.getDarkTheme(userId)) theme.setValue("Ciemny");
            else theme.setValue("Jasny");
            theme.addValueChangeListener(valueChange -> {
                boolean mode = valueChange.getValue().equals("Ciemny");
                userService.setDarkTheme(userId, mode);
                if (mode)
                    UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"dark\")");
                else
                    UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"white\")");
            });

            FormLayout otherForm = new FormLayout(scheduleHours, setHours, theme);

//DELETE ACCOUNT
            Button deleteButton = new Button("Usuń konto");
            deleteButton.getStyle().set("color", "red");
            deleteButton.addClickListener(buttonClickEvent -> {
                Dialog dialog = new Dialog();
                Button button = new Button("Potwierdź");
                button.getStyle().set("color", "red");
                button.setWidth("100%");
                button.addClickListener(buttonClickEvent1 -> {
                    userService.deleteUser(userId);
                    UI.getCurrent().navigate("login");
                    VaadinSession.getCurrent().close();
                });
                dialog.add(button);
                dialog.open();
            });
            deleteButton.setWidth("100%");

            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.add(accountLabel, new Line(), accountForm, groupsLabel, new Line(), groupsForm, groupButton, other, new Line(), otherForm, new Line(), deleteButton);
            if (userService.getGroupCode(userId) != 0) {
                if (groupsService.getLeaderId(userService.getGroupCode(userId)).compareTo(userId) == 0) {
                    groupButton.setText("Usuń grupę");
                    groupButton.addClickListener(click -> deleteGroup(userId, groupsService, userService));
                } else {
                    groupButton.setText("Wyjdź z grupy");
                    groupButton.addClickListener(click -> leaveGroup(userId, userService));
                }
                groupButton.setVisible(true);
            }
            if (VaadinSession.getCurrent().getBrowser().getBrowserApplication().contains("Mobile"))
                verticalLayout.setWidth("100%");
            else verticalLayout.setWidth("50%");
            add(verticalLayout);
        }
    }

    private void leaveGroup(UUID userId, UserService userService) {
        userService.setGroupCode(userId, 0);
        UI.getCurrent().getPage().reload();
    }

    private void deleteGroup(UUID userId, GroupsService groupsService, UserService userService) {
        groupsService.deleteGroup(userService.getGroupCode(userId));
        userService.setGroupCode(userId, 0);
        UI.getCurrent().getPage().reload(); //TODO DYNAMICZNIE ZAMIAST RELOAD + PRZYCISK USUWANIA WIDOCZNY TYLKO JAK MASZ GRUPE

        SuccessNotification notification = new SuccessNotification("Usunięto grupę");
        notification.open();
    }
}