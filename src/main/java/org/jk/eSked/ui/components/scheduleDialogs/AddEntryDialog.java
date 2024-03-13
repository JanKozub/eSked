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

import java.util.ArrayList;
import java.util.UUID;

public class AddEntryDialog extends Dialog {
    private final ComboBox<String> comboBox = new ComboBox<>();
    private final TextField textField = new TextField();

    public AddEntryDialog(ScheduleService scheduleService, int hour, int day, boolean replaceable) {
        UUID userId = SessionService.getUserId();

        Label label = new Label(getTranslation("schedule.dialog.new.title"));

        ArrayList<String> lessons = new ArrayList<>();
        for (int i = 1; i <= 15; i++) lessons.add(getTranslation("lesson." + i));

        comboBox.setItems(lessons);
        comboBox.setPlaceholder(getTranslation("schedule.dialog.new.pick.lesson"));
        comboBox.setWidth("100%");

        textField.setPlaceholder(getTranslation("schedule.dialog.new.own.lesson"));

        HorizontalLayout layout = new HorizontalLayout(comboBox, textField);

        Button addButton = new Button(getTranslation("add"), event -> {
            String name = "";
            if (isNonEmpty(comboBox.getValue())) name = comboBox.getValue();

            if (isNonEmpty(textField.getValue())) {
                if (!name.isEmpty()) {
                    setError(getTranslation("schedule.dialog.new.error.1"));
                    return;
                }
                name = textField.getValue();
            }

            if (name.isEmpty()) {
                setError(getTranslation("schedule.dialog.new.error.2"));
                return;
            }

            close();

            ScheduleEntry scheduleEntry = new ScheduleEntry(userId, hour, day, name, TimeService.now());

            if (replaceable) scheduleService.deleteScheduleEntry(userId, hour, day);
            scheduleService.addScheduleEntry(scheduleEntry);

        });
        addButton.setWidth("100%");
        addButton.addClickShortcut(Key.ENTER);

        VerticalLayout verticalLayout = new VerticalLayout(label, layout, addButton);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        if (SessionService.isSessionMobile()) {
            textField.setWidth("100%");
            VerticalLayout mobileLayout = new VerticalLayout(label, textField, comboBox, addButton);
            mobileLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            add(mobileLayout);

        } else add(verticalLayout);

    }

    private void setError(String msg) {
        comboBox.setErrorMessage(msg);
        textField.setInvalid(true);
        comboBox.setInvalid(true);
    }

    private boolean isNonEmpty(String s) {
        return s != null && !s.isEmpty();
    }
}
