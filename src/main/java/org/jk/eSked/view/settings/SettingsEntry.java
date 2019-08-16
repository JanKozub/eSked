package org.jk.eSked.view.settings;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.components.dialogs.SimplePopup;
import org.jk.eSked.model.User;
import org.jk.eSked.model.entry.ScheduleEntry;
import org.jk.eSked.services.GroupSynService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

class SettingsEntry extends VerticalLayout {
    private final UUID userId;

    SettingsEntry(UserService userService, ScheduleService scheduleService, GroupsService groupsService, GroupSynService groupSynService, SettingsEntryType type) {
        this.userId = VaadinSession.getCurrent().getAttribute(User.class).getId();

        SimplePopup simplePopup = new SimplePopup();

        Label label = new Label("Nazwa");

        TextField textField = new TextField();
        textField.setWidth("100%");

        Button button = new Button("Zmień");
        button.setWidth("100%");

        switch (type) {
            case USERNAME:
                label.setText("Nazwa");
                textField.setValue(VaadinSession.getCurrent().getAttribute(User.class).getUsername());
                textField.setReadOnly(true);
                textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
                textField.setErrorMessage("Pole nie może być puste");
                button.addClickListener(event -> {
                    textField.setValue("");
                    textField.setReadOnly(false);
                    textField.setPlaceholder("Nowa Nazwa");
                    button.setText("Potwierdź");
                    button.addClickListener(e -> {
                        System.out.println("val = " + textField.getValue());
                        Collection<String> usernames = userService.getUsernames();
                        if (!textField.getValue().equals("") && !usernames.contains(textField.getValue())) {
                            textField.setInvalid(false);
                            userService.changeUsername(userId, textField.getValue());
                        } else textField.setInvalid(true);
                        textField.setValue(textField.getValue());
                        textField.setReadOnly(true);
                        button.setText("Zmień");
                    });
//                    textField.clear();
//                    textField.setReadOnly(false);
//                    textField.setPlaceholder("Nowa Nazwa");
//                    textField.setErrorMessage("Pole nie może być puste");
//                    button.setText("Potwierdź");
//                    button.addClickListener(e -> {
//                        System.out.println("nazwa: "  + textField.getValue());
//                        Collection<String> usernames = userService.getUsernames();
//                        if (!textField.getValue().equals("") && !usernames.contains(textField.getValue())) {
//                            userService.changeUsername(userId, textField.getValue());
//                        }else textField.setInvalid(true);
//                        textField.setValue("Nazwa: " + textField.getValue());
//                        textField.setReadOnly(true);
//                        button.setText("Zmień");
//                    });
                });
                break;
            case PASSWORD:
                label.setText("Hasło");
                textField.setVisible(false);

                button.addClickListener(event -> {
                    Dialog dialog = new Dialog();

                    PasswordField oldPassowrd = new PasswordField();
                    oldPassowrd.setPlaceholder("aktualne hasło");
                    PasswordField newPassword1 = new PasswordField();
                    newPassword1.setPlaceholder("Wpisz nowe hasło");
                    PasswordField newPassword2 = new PasswordField();
                    newPassword2.setPlaceholder("Powtórz nowe Hasło ");
                    Button confirm = new Button("Potwierdź", event1 -> {
                        if (User.encodePassword(oldPassowrd.getValue()).equals(VaadinSession.getCurrent().getAttribute(User.class).getPassword())) {
                            if (newPassword1.getValue().equals(newPassword2.getValue()) && !newPassword1.getValue().equals("")) {
                                userService.changePassword(userId, User.encodePassword(newPassword2.getValue()));
                                dialog.close();
                            } else {
                                newPassword2.clear();
                                simplePopup.open("Hasła nie są identyczne");
                            }
                        } else {
                            oldPassowrd.clear();
                            simplePopup.open("aktualne hasło niepoprawne!");
                        }
                    });
                    confirm.setWidth("100%");

                    VerticalLayout layout = new VerticalLayout(oldPassowrd, newPassword1, newPassword2, confirm);
                    dialog.add(layout);
                    dialog.open();
                });
                break;
            case EMAIL:
                textField.setEnabled(false);
                label.setText("Email");
                textField.setValue("Email: " + VaadinSession.getCurrent().getAttribute(User.class).getEmail());

                button.addClickListener(event -> {
                    Dialog dialog = new Dialog();

                    TextField newEmail = new TextField();
                    newEmail.setPlaceholder("Nowy Email");
                    Button confirm = new Button("Potwierdź", event1 -> {
                        if (!newEmail.getValue().equals("") && newEmail.getValue().contains("@")) {
                            userService.changeEmail(userId, textField.getValue());
                            textField.setPlaceholder("Email: " + newEmail.getValue());
                            dialog.close();
                        } else {
                            simplePopup.open("Pole jest puste lub ma niewłaściwą wartość");
                        }
                    });
                    confirm.setWidth("100%");

                    VerticalLayout layout = new VerticalLayout(newEmail, confirm);
                    dialog.add(layout);
                    dialog.open();
                });
                break;
            case SETGROUPCODE:
                textField.setEnabled(false);
                label.setText("Twoja Grupa");
                textField.setValue("Nazwa: " + groupsService.getGroupName(userService.getGroupCode(userId)));
                button.setText("Zmień Grupę");
                button.addClickListener(event -> {
                    Dialog dialog = new Dialog();

                    TextField newCode = new TextField();
                    newCode.setPlaceholder("Nazwa nowej grupy");

                    Button confirm = new Button("Potwierdź", event1 -> {
                        int code = groupsService.doesGroupExist(newCode.getValue());
                        if (code != 0 && groupsService.isGroupAccepted(code)) {
                            userService.setGroupCode(userId, code);
                            textField.setValue("Nazwa: " + groupsService.getGroupName(userService.getGroupCode(userId)));
                            groupSynService.SynchronizeWGroup(userId, code);
                        } else {
                            simplePopup.open("Grupa nie istnije.");
                        }
                        dialog.close();
                    });
                    confirm.setWidth("100%");

                    VerticalLayout layout = new VerticalLayout(newCode, confirm);
                    dialog.add(layout);
                    dialog.open();
                });
                break;
            case AUTOSYNWGROUP:
                label.setText("Automatyczna synchronizacja z grupą");

                textField.setVisible(false);
                if (userService.isSynWGroup(userId)) button.setText("Wyłącz Synchronizacje");
                else button.setText("Włącz Synchronizacje");
                button.addClickListener(event -> {
                    userService.setSynWGroup(userId, !userService.isSynWGroup(userId));
                    if (userService.isSynWGroup(userId)) button.setText("Wyłącz Synchronizacje");
                    else button.setText("Włącz Synchronizacje");
                });
                break;
            case SYNWGROUP:
                label.setText("Synchronizuj z grupą");

                textField.setVisible(false);
                button.setText("Synchronizuj");
                button.addClickListener(event -> {
                    int code = userService.getGroupCode(userId);
                    if (code != 0)
                        groupSynService.SynchronizeWGroup(userId, code);
                    else simplePopup.open("Użytkownik nie jest w żadnej grupie");
                });
                break;
            case ADDGROUP:
                label.setText("Dodaj Grupę ");

                textField.setPlaceholder("Nazwa");
                textField.setWidth("100%");
                button.setText("Dodaj");
                button.addClickListener(event -> {
                    String name = textField.getValue();
                    SimplePopup popup = new SimplePopup();
                    if (name.equals("")) {
                        popup.open("Pole nazwa nie może być puste.");
                    } else {
                        Collection<ScheduleEntry> entries = scheduleService.getScheduleEntries(userId);
                        if (entries.size() != 0) {
                            Random random = new Random();
                            int groupCode = random.nextInt(10000);
                            for (ScheduleEntry entry : entries) {
                                groupsService.addEntryToGroup(false, name, groupCode, userId, entry.getHour(), entry.getDay(), entry.getSubject(),
                                        entry.getCreatedTimestamp());
                            }
                            textField.setValue("");
                            popup.open("Dodano!");
                        } else {
                            textField.setValue("");
                            popup.open("Twoja tabela nie może być pusta.");
                        }
                    }
                });
                break;
            case CHANGETHEME:
                textField.setVisible(false);
                label.setText("Ciemny wygląd strony");
                button.setText("Przełącz");
                button.addClickListener(e -> {
                    userService.setDarkTheme(userId, !userService.getDarkTheme(userId));
                    UI.getCurrent().getPage().reload();
                });
                break;
            case TURNHOURS:
                textField.setVisible(false);
                label.setText("Godziny w planie lekcji");
                boolean state = userService.getScheduleHours(userId);
                if (state) {
                    button.setText("Wyłącz");
                } else button.setText("Włącz");
                button.addClickListener(e -> {
                    userService.setScheduleHours(userId, !state);
                    if (userService.getScheduleHours(userId)) {
                        button.setText("Wyłącz");
                    } else button.setText("Włącz");
                });
                break;
            case DELETEACC:
                textField.setVisible(false);
                label.setText("Usuń konto");
                Button deleteConfirm = new Button("Potwierdz usunięcie konta", event -> {
                    userService.deleteUser(userId);
                    UI.getCurrent().navigate("login");
                    VaadinSession.getCurrent().close();
                });
                Dialog deleteDialog = new Dialog(deleteConfirm);
                button.setText("Usuń");
                button.getStyle().set("color", "red");
                button.addClickListener(event -> deleteDialog.open());
                break;
        }
        setWidth("50%");
        setAlignItems(Alignment.CENTER);
        add(label, textField, button);
    }
}