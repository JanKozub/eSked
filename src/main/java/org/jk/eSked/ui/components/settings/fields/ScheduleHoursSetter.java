package org.jk.eSked.ui.components.settings.fields;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.schedule.ScheduleHour;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.user.HoursService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import javax.validation.ValidationException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleHoursSetter extends VerticalLayout {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private final ArrayList<ScheduleHour> scheduleHours = new ArrayList<>();

    public ScheduleHoursSetter(UUID userId, HoursService hoursService) {
        AtomicInteger currentHour = new AtomicInteger(1);
        int maxHour = hoursService.getScheduleMaxHour(userId);

        Label name = new Label("Ustaw godziny(" + currentHour.get() + "z" + maxHour + ")");
        name.getStyle().set("margin-left", "auto");
        name.getStyle().set("margin-right", "auto");

        TimeField from = new TimeField("Od");
        TimeField to = new TimeField("Do");

        Button confirm = new Button("Następny", b ->
                checkValue(from, to, name, b.getSource(), userId, hoursService, currentHour, maxHour));
        confirm.setWidth("100%");
        confirm.addClickShortcut(Key.ENTER);

        add(name, from, to, confirm);
    }

    private void checkValue(TimeField from, TimeField to, Label name, Button confirm, UUID userId,
                            HoursService hoursService, AtomicInteger currentHour, int maxHour) {
        if (currentHour.get() == 1)
            hoursService.deleteScheduleHours(userId);
        try {
            validateFields(from, to);
            from.setInvalid(false);
            to.setInvalid(false);

            scheduleHours.add(new ScheduleHour(userId, currentHour.get(), from.getValue() + "-" + to.getValue()));

            if (currentHour.get() == maxHour) {
                hoursService.setScheduleHours(scheduleHours);
                currentHour.set(1);
                new SuccessNotification("Godziny zostały ustawione", NotificationType.SHORT).open();
            } else {
                currentHour.set(currentHour.get() + 1);
                if (currentHour.get() == maxHour) confirm.setText(getTranslation(locale, "confirm"));
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

    private void validateFields(TimeField from, TimeField to) {
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
