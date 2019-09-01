package org.jk.eSked.view.settings;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.components.Line;
import org.jk.eSked.components.settingsFields.EmailField;
import org.jk.eSked.components.settingsFields.GroupCodeField;
import org.jk.eSked.components.settingsFields.MyPasswordField;
import org.jk.eSked.components.settingsFields.NameField;
import org.jk.eSked.model.ScheduleHour;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.hours.HoursService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.view.MenuView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Route(value = "settings", layout = MenuView.class)
@PageTitle("Ustawienia")
public class SettingsView extends VerticalLayout {


    public SettingsView(LoginService loginService, UserService userService, GroupsService groupsService, HoursService hoursService) {

        if (loginService.checkIfUserIsLogged()) {
            UUID userId = VaadinSession.getCurrent().getAttribute(User.class).getId();
            setSizeFull();
            setAlignItems(Alignment.CENTER);
//ACCOUNT
            Label accountLabel = new Label("Użytkownik");

            FormLayout accountForm = new FormLayout();
            accountForm.add(new NameField(userId, userService));
            accountForm.add(new MyPasswordField(userId, userService));
            accountForm.add(new EmailField(userId, userService));
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
            Button newGroup = new Button("Nowa grupa");
            newGroup.getStyle().set("margin-top", "auto");
            newGroup.getStyle().set("margin-bottom", "auto");
            groupsForm.add(newGroup);
//SCHEDULE
            Label other = new Label("Inne");

            RadioButtonGroup<String> scheduleHours = new RadioButtonGroup<>();
            scheduleHours.setLabel("godziny w planie");
            scheduleHours.setItems("Tak", "Nie");
            if (userService.getScheduleHours(userId)) scheduleHours.setValue("Tak");
            else scheduleHours.setValue("Nie");
            scheduleHours.addValueChangeListener(valueChange -> userService.setScheduleHours(userId, valueChange.getValue().equals("Tak")));

            Button setHours = new Button("Ustaw godziny");
            setHours.getStyle().set("margin-top", "auto");
            setHours.getStyle().set("margin-bottom", "auto");
            setHours.addClickListener(buttonClickEvent -> openDialog(userId, hoursService));

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
            deleteButton.setWidth("100%");
            deleteButton.addClickListener(buttonClickEvent -> {
                Dialog dialog = new Dialog();
                Button button = new Button("Potwierdź");
                button.getStyle().set("color", "red");
                button.addClickListener(buttonClickEvent1 -> {
                    userService.deleteUser(userId);
                    UI.getCurrent().navigate("login");
                    VaadinSession.getCurrent().close();
                });
                dialog.add(button);
                dialog.open();
            });

            VerticalLayout verticalLayout = new VerticalLayout(accountLabel, new Line(), accountForm, groupsLabel, new Line(), groupsForm, other, new Line(), otherForm, new Line(), deleteButton);
            verticalLayout.setWidth("50%");
            add(verticalLayout);
        }
    }

    private void openDialog(UUID userId, HoursService hoursService) {
        Dialog dialog = new Dialog();
        AtomicInteger currentHour = new AtomicInteger(1);
        Label name = new Label("Ustaw godziny(" + currentHour + "z" + hoursService.getScheduleMaxHour(userId) + ")");
        name.getStyle().set("margin-left", "auto");
        name.getStyle().set("margin-right", "auto");
        HorizontalLayout nameLabel = new HorizontalLayout(name);
        nameLabel.setWidth("100%");

        TextField fromHour = new TextField("Od");
        fromHour.setValueChangeMode(ValueChangeMode.TIMEOUT);
        fromHour.setValueChangeTimeout(50);

        AtomicInteger lastReadFromHour = new AtomicInteger();
        fromHour.addValueChangeListener(event -> isInputValueValid(fromHour, lastReadFromHour));

        TextField toHour = new TextField("Do");
        toHour.setValueChangeMode(ValueChangeMode.TIMEOUT);
        toHour.setValueChangeTimeout(50);

        AtomicInteger lastReadToHour = new AtomicInteger();
        toHour.addValueChangeListener(event -> isInputValueValid(toHour, lastReadToHour));

        VerticalLayout timeLayout = new VerticalLayout(fromHour, toHour);

        Icon icon = new Icon(VaadinIcon.ARROW_DOWN);
        icon.getStyle().set("height", "100%");

        HorizontalLayout middleLayout = new HorizontalLayout(timeLayout, icon);
        middleLayout.setVerticalComponentAlignment(Alignment.CENTER, icon);
        Button confirm = new Button("Następny");
        confirm.setWidth("100%");
        List<ScheduleHour> hoursList = new ArrayList<>();

        confirm.addClickListener(event -> {
            if (currentHour.get() == hoursService.getScheduleMaxHour(userId) - 1) confirm.setText("Potwierdź");

            if (!fromHour.isEmpty()) {
                fromHour.setInvalid(false);
                if (!toHour.isEmpty()) {
                    toHour.setInvalid(false);
                    if (fromHour.getValue().matches("(?:[0-1][0-9]|2[0-4]):[0-5]\\d")) {
                        fromHour.setInvalid(false);
                        if (toHour.getValue().matches("(?:[0-1][0-9]|2[0-4]):[0-5]\\d")) {
                            toHour.setInvalid(false);
                            hoursList.add(new ScheduleHour(userId, currentHour.get(), fromHour.getValue() + "-" + toHour.getValue()));
                            currentHour.set(currentHour.get() + 1);
                            name.setText("Ustaw godziny(" + currentHour + "z" + hoursService.getScheduleMaxHour(userId) + ")");
                            if (currentHour.get() == hoursService.getScheduleMaxHour(userId) + 1) {
                                hoursService.deleteScheduleHours(userId);
                                hoursService.setScheduleHours(hoursList);
                                dialog.close();
                            }
                            fromHour.clear();
                            toHour.clear();
                        } else {
                            toHour.setErrorMessage("Podana wartość nie jest godziną");
                            toHour.setInvalid(true);
                        }
                    } else {
                        fromHour.setErrorMessage("Podana wartość nie jest godziną");
                        fromHour.setInvalid(true);
                    }
                } else {
                    toHour.setErrorMessage("Pole nie może być puste");
                    toHour.setInvalid(true);
                }
            } else {
                fromHour.setErrorMessage("Pole nie może być puste");
                fromHour.setInvalid(true);
            }
        });

        dialog.add(nameLabel, middleLayout, confirm);

        dialog.setWidth("100%");
        dialog.setHeight("50%");
        dialog.open();
    }

    private void isInputValueValid(TextField textField, AtomicInteger integer) {
        int length = textField.getValue().length();
        if (!textField.isEmpty()) {
            if (Character.isLetter(textField.getValue().charAt(length - 1))) textField.setInvalid(true);
        } else {
            textField.setErrorMessage("Pole nie może być puste");
            textField.setInvalid(true);
        }
        if (length == 2 && integer.get() == 1) textField.setValue(textField.getValue() + ":");
        if (length == 3 && integer.get() == 4) textField.setValue(textField.getValue().substring(0, length - 1));
        if (length > 5) {
            textField.setErrorMessage("Ilość znaków jest zbyt duża");
            textField.setInvalid(true);
        } else textField.setInvalid(false);
        if (length < 5) {
            textField.setErrorMessage("Godzina musi zawierć 4 znaki");
            textField.setInvalid(true);
        } else {
            if (length > 5) {
                textField.setErrorMessage("Ilość znaków jest zbyt duża");
                textField.setInvalid(true);
            } else textField.setInvalid(false);
        }
        integer.set(length);
    }

    private void openGroupDialog(UUID userId, GroupsService groupsService) {//TODO WYSWIETLANIE ZE ADMIN MUSI POTWIERDZIC
        Label name = new Label("Nowa Grupa");
        name.getStyle().set("margin-left", "auto");
        name.getStyle().set("margin-right", "auto");
        HorizontalLayout nameLabel = new HorizontalLayout(name);
        nameLabel.setWidth("100%");

        TextField groupName = new TextField();
        groupName.setWidth("100%");

        Button confirm = new Button("Dodaj");
        confirm.setWidth("100%");
        confirm.addClickListener(event -> {
            if (!groupName.isEmpty()) {
                groupName.setInvalid(false);
                Random random = new Random();
                groupsService.addGroup(userId, groupName.getValue(), random.nextInt(8999) + 1000, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
            } else {
                groupName.setErrorMessage("Pole nie może być puste");
                groupName.setInvalid(true);
            }
        });
        add(nameLabel, groupName, confirm);
    }
}