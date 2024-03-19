package org.jk.esked.app.ui.components.scheduleDialogs;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.esked.app.backend.model.ScheduleEntry;
import org.jk.esked.app.backend.model.User;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.TimeService;

import java.util.ArrayList;

public class AddEntryDialog extends Dialog {
    private final ComboBox<String> comboBox = new ComboBox<>();
    private final TextField textField = new TextField();

    public AddEntryDialog(User user, ScheduleService scheduleService, int hour, int day) {
        Text label = new Text(getTranslation("schedule.dialog.new.title"));

        setCloseOnOutsideClick(true);

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

            ScheduleEntry scheduleEntry = new ScheduleEntry();
            scheduleEntry.setUser(user);
            scheduleEntry.setHour(hour);
            scheduleEntry.setDay(day);
            scheduleEntry.setSubject(name);
            scheduleEntry.setCreatedTimestamp(TimeService.now());

            scheduleService.saveScheduleEntry(scheduleEntry); //TODO update if needed

            close();
        });
        addButton.setWidth("100%");
        addButton.addClickShortcut(Key.ENTER);

        VerticalLayout verticalLayout = new VerticalLayout(label, layout, addButton);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        add(verticalLayout);
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
