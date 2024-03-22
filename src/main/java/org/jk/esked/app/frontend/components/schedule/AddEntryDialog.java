package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.frontend.components.buttons.WideButton;

import java.util.ArrayList;

@CssImport("./styles/add-entry-dialog.css")
public class AddEntryDialog extends Dialog {
    private final ComboBox<String> comboBox = new ComboBox<>();
    private final TextField textField = new TextField();

    public AddEntryDialog(User user, ScheduleEntryService scheduleEntryService, int hour, int day) {
        Text label = new Text(getTranslation("schedule.dialog.new.title"));

        setCloseOnOutsideClick(true);

        ArrayList<String> lessons = new ArrayList<>();
        for (int i = 1; i <= 15; i++) lessons.add(getTranslation("lesson." + i));

        comboBox.setItems(lessons);
        comboBox.setPlaceholder(getTranslation("schedule.dialog.new.pick.lesson"));
        comboBox.addClassName("entry-combo-box");
        textField.setPlaceholder(getTranslation("schedule.dialog.new.own.lesson"));

        HorizontalLayout layout = new HorizontalLayout(comboBox, textField);

        Button addButton = new WideButton(getTranslation("add"), event -> {
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

            scheduleEntryService.save(new ScheduleEntry(user, hour, day, name));

            close();
        });
        addButton.addClickShortcut(Key.ENTER);

        add(new VerticalLayout(label, layout, addButton));
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
