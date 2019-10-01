package org.jk.eSked.ui.views;

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
import org.jk.eSked.app.LoginService;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.model.types.ThemeType;
import org.jk.eSked.backend.service.*;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.myImpl.Line;
import org.jk.eSked.ui.components.myImpl.SuccessNotification;
import org.jk.eSked.ui.components.settings.*;

import java.util.UUID;

@Route(value = "settings", layout = MenuView.class)
@PageTitle("Ustawienia")
public class SettingsView extends VerticalLayout {

    public SettingsView(LoginService loginService, UserService userService, GroupService groupsService, HoursService hoursService, EmailService emailService) {

        groupsService.getGroupCodes();

        if (loginService.checkIfUserIsLogged()) {

            UUID userId = VaadinSession.getCurrent().getAttribute(User.class).getId();

            setSizeFull();
            setAlignItems(Alignment.CENTER);
//ACCOUNT
            Label accountLabel = new Label("Użytkownik");

            FormLayout accountForm = new FormLayout();
            accountForm.add(new NameField(userId, userService, emailService));
            accountForm.add(new MyPasswordField(userId, userService, emailService, true));
            accountForm.add(new EmailField(userId, userService, emailService, true));
            RadioButtonGroup<String> languageGroup = new RadioButtonGroup<>();
            languageGroup.setLabel("Język");
            languageGroup.setItems("Polski", "English");
            languageGroup.setValue("Polski");
            languageGroup.getStyle().set("padding-top", "0px");
            languageGroup.getStyle().set("margin-top", "auto");

            accountForm.add(languageGroup);
            accountForm.add(new Button("Dodaj Przedmioty Do Planu",
                    buttonClickEvent -> UI.getCurrent().navigate("schedule/new")), 2);
//GROUPS
            Label groupsLabel = new Label("Grupy");

            FormLayout groupsForm = new FormLayout();
            groupsForm.add(new GroupCodeField(userId, userService, groupsService));
            Button groupSyn = new Button("Synchronizuj z grupą");
            groupSyn.addClickListener(buttonClickEvent -> groupsService.synchronizeWGroup(userId, userService.getGroupCode(userId)));
            groupSyn.getStyle().set("margin-top", "auto");
            groupsForm.add(groupSyn);

            RadioButtonGroup<String> eventSync = new RadioButtonGroup<>();
            eventSync.setLabel("Synchronizacja z Wydarzeniami");
            eventSync.setItems("Włącz", "Wyłącz");
            if (userService.isEventsSyn(userId)) eventSync.setValue("Włącz");
            else eventSync.setValue("Wyłącz");
            eventSync.addValueChangeListener(valueChange -> userService.setEventsSyn(userId, valueChange.getValue().equals("Włącz")));
            groupsForm.add(eventSync);

            RadioButtonGroup<String> tableSync = new RadioButtonGroup<>();
            tableSync.setLabel("Synchronizacja z Tabelą");
            tableSync.setItems("Włącz", "Wyłącz");
            if (userService.isTableSyn(userId)) tableSync.setValue("Włącz");
            else tableSync.setValue("Wyłącz");
            tableSync.addValueChangeListener(valueChange -> userService.setTableSyn(userId, valueChange.getValue().equals("Włącz")));
            groupsForm.add(tableSync);

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
                if (mode) ThemeService.setTheme(ThemeType.DARK);
                else ThemeService.setTheme(ThemeType.WHITE);
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
    }

    private void deleteGroup(UUID userId, GroupService groupsService, UserService userService) {
        groupsService.deleteGroup(userService.getGroupCode(userId));
        userService.setGroupCode(userId, 0);

        new SuccessNotification("Usunięto grupę", NotificationType.SHORT).open();
    }
}