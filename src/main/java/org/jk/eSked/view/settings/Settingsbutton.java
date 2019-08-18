package org.jk.eSked.view.settings;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.model.User;
import org.jk.eSked.model.entry.ScheduleEntry;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;

import java.util.Collection;
import java.util.Random;


class Settingsbutton extends VerticalLayout {
    private User user = VaadinSession.getCurrent().getAttribute(User.class);

    Settingsbutton(String name, String data, String buttonText, SettingsEntryType entryType, UserService userService, GroupsService groupsService, ScheduleService scheduleService) {
        Label title = new Label(name);

        TextField textField = new TextField();
        textField.setValue(data);
        textField.setWidth("50%");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        textField.setEnabled(false);

        Button button = new Button(buttonText);

        button.setWidth("50%");

        setAlignItems(Alignment.CENTER);

        add(title, textField, button);

        switch (entryType) {
            case USERNAME:
                UsernameOnlick(userService, textField, button);
                break;
            case EMAIL:
                emailOnClick(userService, textField, button);
                break;
            case PASSWORD:
                passwordOnClick(userService, textField, button);
                break;
            case SETGROUPCODE:
                setGroupCodeOnClick(userService, textField, button);
                break;
            case AUTOSYNWGROUP:
                autoSynWGroupOnClick(userService, textField, button);
                break;
            case SYNWGROUP:
                synWGroupOnClick(userService, groupsService, textField, button);
                break;
            case ADDGROUP:
                addGroupOnClick(groupsService, scheduleService, textField, button);
                break;
            case TURNHOURS:
                turnHoursOnClick(userService, textField, button);
                break;
            case CHANGETHEME:
                changeThemeOnClick(userService, textField, button);
                break;
            case DELETEACC:
                deleteAccOnClick(userService, textField, button);
                break;
        }
    }

    private void deleteAccOnClick(UserService userService, TextField textField, Button button) {
        remove(textField);
        Button deleteConfirm = new Button("Potwierdz usunięcie konta", event -> {
            userService.deleteUser(user.getId());
            UI.getCurrent().navigate("login");
            VaadinSession.getCurrent().close();
        });
        deleteConfirm.getStyle().set("color", "red");
        Dialog deleteDialog = new Dialog(deleteConfirm);
        button.getStyle().set("color", "red");
        button.addClickListener(event -> deleteDialog.open());
    }

    private void changeThemeOnClick(UserService userService, TextField textField, Button button) {
        remove(textField);
        if (userService.getDarkTheme(user.getId())) button.setText("Wyłącz");
        else button.setText("Włącz");
        button.addClickListener(buttonClickEvent -> {
            userService.setDarkTheme(user.getId(), !userService.getDarkTheme(user.getId()));
            UI.getCurrent().getPage().reload();
        });
    }

    private void turnHoursOnClick(UserService userService, TextField textField, Button button) {
        remove(textField);
        if (userService.getScheduleHours(user.getId())) button.setText("Wyłącz");
        else button.setText("Włącz");
        button.addClickListener(buttonClickEvent -> {
            userService.setScheduleHours(user.getId(), !userService.getScheduleHours(user.getId()));
            if (userService.getScheduleHours(user.getId())) button.setText("Wyłącz");
            else button.setText("Włącz");
        });
    }

    private void addGroupOnClick(GroupsService groupsService, ScheduleService scheduleService, TextField textField, Button button) {
        remove(textField);
        button.addClickListener(buttonClickEvent -> {
            add(textField);
            remove(button);
            Button button1 = new Button("Potwierdź");
            button1.setWidth("50%");
            add(button1);
            textField.setEnabled(true);
            textField.setPlaceholder("Nazwa nowej grupy");
            button1.addClickListener(buttonClickEvent1 -> {
                if (!textField.getValue().equals("")) {
                    textField.setInvalid(false);
                    Collection<String> groupsNames = groupsService.getGroupsNames();
                    if (!groupsNames.contains(textField.getValue())) {
                        textField.setInvalid(false);
                        Collection<ScheduleEntry> entries = scheduleService.getScheduleEntries(user.getId());
                        if (entries.size() != 0) {
                            Random random = new Random();
                            int groupCode = random.nextInt(10000);
                            for (ScheduleEntry entry : entries) {
                                groupsService.addEntryToGroup(false, textField.getValue(), groupCode, user.getId(), entry.getHour(), entry.getDay(), entry.getSubject(),
                                        entry.getCreatedTimestamp());
                            }
                            Notification notification = new Notification("Grupa została pomyślnie stworzona!", 5000, Notification.Position.TOP_END);
                            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                            notification.open();
                        } else {
                            Notification notification = new Notification("Nie można stworzyć grupy bez wpisów w tabeli", 5000, Notification.Position.TOP_END);
                            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                            notification.open();
                        }
                    } else {
                        Notification notification = new Notification("Grupa z taką nazwą już istnieje", 5000, Notification.Position.TOP_END);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.open();
                    }
                } else {
                    Notification notification = new Notification("Pole z nazwą nie może być puste", 5000, Notification.Position.TOP_END);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();
                }
                remove(button1, textField);
                add(button);
            });
        });
    }

    private void synWGroupOnClick(UserService userService, GroupsService groupsService, TextField textField, Button button) {
        remove(textField);
        button.addClickListener(buttonClickEvent -> {
            int code = userService.getGroupCode(user.getId());
            if (code != 0) {
                groupsService.synchronizeWGroup(user.getId(), code);
            }
        });
    }

    private void autoSynWGroupOnClick(UserService userService, TextField textField, Button button) {
        remove(textField);
        if (userService.isSynWGroup(user.getId())) button.setText("Wyłącz");
        else button.setText("Włącz");
        button.addClickListener(buttonClickEvent -> {
            userService.setSynWGroup(user.getId(), !userService.isSynWGroup(user.getId()));
            if (userService.isSynWGroup(user.getId())) button.setText("Wyłącz");
            else button.setText("Włącz");
        });
    }

    private void setGroupCodeOnClick(UserService userService, TextField textField, Button button) {
        button.addClickListener(buttonClickEvent -> {
            remove(button);
            Button button1 = new Button("Potwierdź");
            button1.setWidth("50%");
            add(button1);
            textField.setEnabled(true);
            textField.setPlaceholder("Nowy kod");
            button1.addClickListener(buttonClickEvent1 -> {
                if (!textField.getValue().equals("")) {
                    textField.setInvalid(false);
                    if (textField.getValue().matches("[0-9]+")) {
                        textField.setInvalid(false);
                        if (textField.getValue().length() == 4) {
                            textField.setInvalid(false);
                            userService.setGroupCode(user.getId(), Integer.parseInt(textField.getValue()));
                            Notification notification = new Notification("Kod został zmieniony na \"" + textField.getValue() + "\"", 5000, Notification.Position.TOP_END);
                            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                            notification.open();
                        } else {
                            textField.setErrorMessage("Kod musi zawierać 4 liczby");
                            textField.setInvalid(true);
                        }
                    } else {
                        textField.setErrorMessage("Kod musi być liczbą");
                        textField.setInvalid(true);
                    }
                } else {
                    textField.setErrorMessage("Pole nie może być puste");
                    textField.setInvalid(true);
                }
                textField.setValue(Integer.toString(userService.getGroupCode(user.getId())));
                textField.setEnabled(false);
                remove(button1);
                add(button);
            });
        });
    }

    private void passwordOnClick(UserService userService, TextField textField, Button button) {
        remove(textField);
        button.addClickListener(buttonClickEvent -> {
            add(textField);
            remove(button);
            Button button1 = new Button("Potwierdź");
            button1.setWidth("50%");
            add(button1);
            textField.setEnabled(true);
            textField.setPlaceholder("Nowe Hasło");
            button1.addClickListener(buttonClickEvent1 -> {
                if (!textField.getValue().equals("")) {
                    textField.setInvalid(false);
                    userService.changePassword(user.getId(), User.encodePassword(textField.getValue()));
                    Notification notification = new Notification("Hasło zostało zmienione!", 5000, Notification.Position.TOP_END);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.open();
                } else {
                    Notification notification = new Notification("Pole nie może być puste!", 5000, Notification.Position.TOP_END);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();
                }
                remove(button1, textField);
                add(button);
            });
        });
    }

    private void emailOnClick(UserService userService, TextField textField, Button button) {
        button.addClickListener(buttonClickEvent -> {
            remove(button);
            Button button1 = new Button("Potwierdź");
            button1.setWidth("50%");
            add(button1);
            textField.setEnabled(true);
            textField.setPlaceholder("Nowy emial");
            button1.addClickListener(buttonClickEvent1 -> {
                if (!textField.getValue().equals("")) {
                    textField.setInvalid(false);
                    if (textField.getValue().contains("@")) {
                        textField.setInvalid(false);
                        Collection<String> emails = userService.getEmails();
                        if (!emails.contains(textField.getValue())) {
                            textField.setInvalid(false);
                            userService.changeEmail(user.getId(), textField.getValue());

                            Notification notification = new Notification("Zmieniono email na \"" + textField.getValue() + "\"", 5000, Notification.Position.TOP_END);
                            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                            notification.open();
                        } else {
                            textField.setErrorMessage("Ten email jest już zarejstorowany");
                            textField.setInvalid(true);
                        }
                    } else {
                        textField.setErrorMessage("Wprowadzony tekst nie jest email'em");
                        textField.setInvalid(true);
                    }
                } else {
                    textField.setErrorMessage("Pole z nowym emailem nie może być puste");
                    textField.setInvalid(true);
                }
                textField.setValue(userService.getEmail(user.getId()));
                textField.setEnabled(false);
                remove(button1);
                add(button);
            });
        });
    }

    private void UsernameOnlick(UserService userService, TextField textField, Button button) {
        button.addClickListener(buttonClickEvent -> {
            remove(button);
            Button button1 = new Button("Potwierdź");
            button1.setWidth("50%");
            add(button1);
            textField.setEnabled(true);
            textField.setPlaceholder("Nowa nazwa");
            button1.addClickListener(buttonClickEvent1 -> {
                if (!textField.getValue().equals("")) {
                    textField.setInvalid(false);
                    Collection<String> usernames = userService.getUsernames();
                    if (!usernames.contains(textField.getValue())) {
                        textField.setInvalid(false);
                        userService.changeUsername(user.getId(), textField.getValue());

                        Notification notification = new Notification("Zmieniono nazwę na \"" + textField.getValue() + "\"", 5000, Notification.Position.TOP_END);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.open();
                    } else {
                        textField.setErrorMessage("Użytkownik z taką nazwą istnieje");
                        textField.setInvalid(true);
                    }
                } else {
                    textField.setErrorMessage("Pole z nową nazwą nie może być puste");
                    textField.setInvalid(true);
                }
                textField.setValue(userService.getUserName(user.getId()));
                textField.setEnabled(false);
                remove(button1);
                add(button);
            });
        });
    }
}