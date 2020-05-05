package org.jk.eSked.ui.components.scheduleDialogs;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.ScheduleService;

import java.util.UUID;

public class ScheduleDialogs {
    private final ScheduleService scheduleService;
    private final UUID userId;
    private Dialog dialog;

    public ScheduleDialogs(ScheduleService scheduleService, UUID userId) {
        this.scheduleService = scheduleService;
        this.userId = userId;
    }


    public Dialog addEntryDialog(int day, int hour, boolean replaceable) {
        dialog = new Dialog();

        Label label = new Label("Nowy przedmiot");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems("INF", "J.Niem", "J.ANG", "J.POL", "MAT", "CHEM", "BIOL",
                "FIZ", "GEO", "WF", "REL", "HIST", "EDB", "WOS", "PLA", "G.W.");
        comboBox.setPlaceholder("Wybierz przedmiot");
        comboBox.setWidth("100%");

        TextField textField = new TextField();
        textField.setPlaceholder("Własny przedmiot");

        HorizontalLayout layout = new HorizontalLayout(comboBox, textField);

        Button addButton;
        addButton = new Button("Dodaj!", event -> {
            String name = "";
            if (comboBox.getValue() != null && !comboBox.getValue().equals("")) {
                name = comboBox.getValue();
            }

            if (!textField.getValue().equals("")) {
                name = textField.getValue();
            }

            if (!name.equals("")) {
                if (comboBox.getValue() == null || comboBox.getValue().equals("") || textField.getValue().equals("")) {
                    ScheduleEntry scheduleEntry = new ScheduleEntry(userId, hour, day, name, TimeService.now());
                    dialog.close();
                    if (replaceable) {
                        scheduleService.deleteScheduleEntry(userId, hour, day);
                    }
                    scheduleService.addScheduleEntry(scheduleEntry);
                } else {
                    comboBox.setErrorMessage("Można wybrać tylko jedna opcje");
                    comboBox.setInvalid(true);
                    textField.setInvalid(true);
                    comboBox.clear();
                    textField.clear();
                }
            } else {
                comboBox.setErrorMessage("Jedno z pól nie moze być puste");
                textField.setInvalid(true);
                comboBox.setInvalid(true);
            }
        });
        addButton.setWidth("100%");
        addButton.addClickShortcut(Key.ENTER);

        VerticalLayout verticalLayout = new VerticalLayout(label, layout, addButton);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        if (SessionService.isSessionMobile()) {
            textField.setWidth("100%");
            VerticalLayout mobileLayout = new VerticalLayout(label, textField, comboBox, addButton);
            mobileLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            dialog.add(mobileLayout);

        } else dialog.add(verticalLayout);

        return dialog;
    }

    public Dialog deleteEntryDialog(ScheduleEntry entry) {
        Dialog dialog = new Dialog();

        Label label = new Label("Aktualny przedmiot:");
        Label name = new Label(entry.getSubject());

        Button deleteButton = new Button("Usuń", event -> {
            scheduleService.deleteScheduleEntry(userId, entry.getHour(), entry.getDay());
            dialog.close();
        });
        deleteButton.getStyle().set("color", "red");
        deleteButton.setWidth("10vw");

        Button switchButton = new Button("Zamień", event -> {
            Dialog addDialog = addEntryDialog(entry.getDay(), entry.getHour(), true);
            addDialog.addDetachListener(e -> {
                dialog.close();
            });
            addDialog.open();
        });
        switchButton.getStyle().set("color", "green");
        switchButton.setWidth("10vw");

        HorizontalLayout buttons = new HorizontalLayout(deleteButton, switchButton);

        VerticalLayout layout = new VerticalLayout(label, name, buttons);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        dialog.setWidth("30vw");
        dialog.add(layout);
        return dialog;
    }
}
