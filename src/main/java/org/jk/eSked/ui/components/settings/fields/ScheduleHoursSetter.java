package org.jk.eSked.ui.components.settings.fields;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import org.jk.eSked.backend.model.schedule.ScheduleHour;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.user.HoursService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import javax.validation.ValidationException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleHoursSetter extends VerticalLayout {
    private HoursService hoursService;
    private UUID userId;
    private TimeField from;
    private TimeField to;
    private Button confirm;
    private Label name;
    private AtomicInteger currentHour;
    private ArrayList<ScheduleHour> scheduleHours = new ArrayList<>();
    private int maxHour;

    public ScheduleHoursSetter(UUID userId, HoursService hoursService) {
        this.userId = userId;
        this.hoursService = hoursService;
        currentHour = new AtomicInteger(1);
        maxHour = hoursService.getScheduleMaxHour(userId);

        name = new Label("Ustaw godziny(" + currentHour.get() + "z" + maxHour + ")");
        name.getStyle().set("margin-left", "auto");
        name.getStyle().set("margin-right", "auto");

        from = new TimeField("Od");
        to = new TimeField("Do");

        confirm = new Button("Następny", this::checkValue);
        confirm.setWidth("100%");
        confirm.addClickShortcut(Key.ENTER);

        add(name, from, to, confirm);
    }

    private void checkValue(ClickEvent event) {
        if (currentHour.get() == 1)
            hoursService.deleteScheduleHours(userId);
        try {
            validateFields();
            from.setInvalid(false);
            to.setInvalid(false);

            scheduleHours.add(new ScheduleHour(userId, currentHour.get(), from.getValue() + "-" + to.getValue()));

            if (currentHour.get() == maxHour) {
                hoursService.setScheduleHours(scheduleHours);
                currentHour.set(1);
                new SuccessNotification("Godziny zostały ustawione", NotificationType.SHORT).open();
            } else {
                currentHour.set(currentHour.get() + 1);
                if (currentHour.get() == maxHour) confirm.setText("Potwierdź");
            }
            to.clear();
            from.clear();
            name.setText("Ustaw godziny(" + currentHour.get() + "z" + maxHour + ")");
        } catch (ValidationException ex) {
            to.setErrorMessage(ex.getMessage());
            from.setInvalid(true);
            to.setInvalid(true);
        }
    }

    private void validateFields() {
        if (from.isEmpty() || to.isEmpty()) throw new ValidationException("Oba pola muszą być wypełnione");

    }

    private static class TimeField extends TimePicker {
        private TimeField(String label) {
            super(label);
            setClearButtonVisible(true);
            setMin("07:00");
            setMax("20:00");
            setStep(Duration.ofMinutes(15));
            setWidth("100%");
        }
    }
}
