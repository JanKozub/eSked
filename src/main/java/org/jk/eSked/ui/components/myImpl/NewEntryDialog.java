package org.jk.eSked.ui.components.myImpl;

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
import org.jk.eSked.backend.service.user.ScheduleService;

import java.time.Instant;
import java.util.UUID;

public abstract class NewEntryDialog extends Dialog {
    private final ScheduleService scheduleService;
    private final UUID userId;

    protected NewEntryDialog(int day, int hour, ScheduleService scheduleService, boolean isMobile) {
        this.scheduleService = scheduleService;
        this.userId = SessionService.getUserId();

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
                    addEntry(day, hour, name);
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

        if (isMobile) {
            textField.setWidth("100%");
            VerticalLayout mobileLayout = new VerticalLayout(label, textField, comboBox, addButton);
            mobileLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            add(mobileLayout);

        } else add(verticalLayout);

        open();
    }

    private void addEntry(int day, int hour, String name) {
        ScheduleEntry scheduleEntry = new ScheduleEntry(userId, hour, day, name, Instant.now().toEpochMilli());
        scheduleService.addScheduleEntry(scheduleEntry);
        close();
        refresh();
    }

    public abstract void refresh();
}