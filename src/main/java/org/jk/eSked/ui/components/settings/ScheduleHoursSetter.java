package org.jk.eSked.ui.components.settings;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import org.jk.eSked.backend.model.ScheduleHour;
import org.jk.eSked.backend.service.HoursService;
import org.jk.eSked.ui.components.myImpl.SuccessNotification;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleHoursSetter extends VerticalLayout {
    private final List<ScheduleHour> hoursList = new ArrayList<>();
    private final HoursService hoursService;
    private final Label name;
    private final TextField fromHour = new TextField("Od");
    private final TextField toHour = new TextField("Do");
    private final Button confirm;
    private final UUID userId;
    private Registration registration;
    private AtomicInteger currentHour;

    public ScheduleHoursSetter(UUID userId, HoursService hoursService) {
        this.hoursService = hoursService;
        this.userId = userId;

        AtomicInteger lastReadFromHour = new AtomicInteger(0);
        AtomicInteger lastReadToHour = new AtomicInteger(0);

        name = new Label();
        name.getStyle().set("margin-left", "auto");
        name.getStyle().set("margin-right", "auto");

        fromHour.setValueChangeMode(ValueChangeMode.EAGER);
        fromHour.setWidth("100%");
        fromHour.addValueChangeListener(event -> isInputValueValid(fromHour, lastReadFromHour));

        toHour.setValueChangeMode(ValueChangeMode.EAGER);
        toHour.setWidth("100%");
        toHour.addValueChangeListener(event -> isInputValueValid(toHour, lastReadToHour));

        confirm = new Button("Następny");
        confirm.setWidth("100%");
        confirm.addClickShortcut(Key.ENTER);

        add(mainLayout());
    }

    private VerticalLayout mainLayout() {
        Grid<ScheduleHour> hours = new Grid<>();

        removeAll();
        currentHour = new AtomicInteger(1);
        hoursList.clear();
        fromHour.setInvalid(false);
        toHour.setInvalid(false);

        name.setText("Ustaw godziny(" + currentHour.get() + "z" + hoursService.getScheduleMaxHour(userId) + ")");
        HorizontalLayout nameLabel = new HorizontalLayout(name);
        nameLabel.setWidth("100%");

        VerticalLayout timeLayout = new VerticalLayout(fromHour, toHour);

        if (currentHour.get() == hoursService.getScheduleMaxHour(userId)) {
            confirm.setText("Potwierdź");

            registration = confirm.addClickListener(click -> {
                if (buttonValidation(fromHour) && buttonValidation(toHour)) {

                    hoursList.add(new ScheduleHour(userId, currentHour.get(), fromHour.getValue() + "-" + toHour.getValue()));

                    fromHour.clear();
                    toHour.clear();
                    fromHour.setInvalid(false);
                    toHour.setInvalid(false);

                    submitHours(click);
                }
            });
        } else {
            confirm.setText("Następny");
            registration = confirm.addClickListener(buttonClickEvent -> {
                addHour(buttonClickEvent);
                hours.setItems(hoursList);
            });
        }

        hours.addColumn(new TextRenderer<>(scheduleHour -> Integer.toString(scheduleHour.getHour()))).setHeader("Godzina").setAutoWidth(true).setFlexGrow(0);
        hours.addColumn(new TextRenderer<>(ScheduleHour::getData)).setHeader("Info").setAutoWidth(true).setFlexGrow(0);
        hours.recalculateColumnWidths();
        hours.setHeightByRows(true);
        hours.setItems(hoursService.getHours(userId));

        return new VerticalLayout(nameLabel, timeLayout, confirm, hours);
    }

    private void addHour(ClickEvent clickEvent) {

        if (currentHour.get() == hoursService.getScheduleMaxHour(userId) - 1) {
            confirm.setText("Potwierdź");
            registration.remove();
            registration = confirm.addClickListener(this::submitHours);
        }
        if (buttonValidation(fromHour) && buttonValidation(toHour)) {

            hoursList.add(new ScheduleHour(userId, currentHour.get(), fromHour.getValue() + "-" + toHour.getValue()));
            currentHour.set(currentHour.get() + 1);
            name.setText("Ustaw godziny(" + currentHour.get() + "z" + hoursService.getScheduleMaxHour(userId) + ")");

            fromHour.clear();
            toHour.clear();
            fromHour.setInvalid(false);
            toHour.setInvalid(false);

            fromHour.focus();
        }
    }

    private void submitHours(ClickEvent clickEvent) {
        hoursList.add(new ScheduleHour(userId, currentHour.get(), fromHour.getValue() + "-" + toHour.getValue()));
        hoursService.deleteScheduleHours(userId);
        hoursService.setScheduleHours(hoursList);
        removeAll();
        SuccessNotification notification = new SuccessNotification("Godziny zostały pomyślnie dodane");
        notification.open();
        registration.remove();
        fromHour.clear();
        toHour.clear();
        Button again = new Button("Ustaw jeszcze raz", buttonClickEvent -> add(mainLayout()));
        again.setWidth("100%");
        add(again);
    }

    private boolean buttonValidation(TextField textField) {
        if (textField.isEmpty()) {
            textField.setErrorMessage("Pole nie może być puste");
            textField.setInvalid(true);
        }
        return !textField.isInvalid();
    }

    private void isInputValueValid(TextField textField, AtomicInteger integer) {
        int length = textField.getValue().length();

        if (length == 2 && integer.get() == 1) textField.setValue(textField.getValue() + ":");

        if (length == 3 && integer.get() == 4) textField.setValue(textField.getValue().substring(0, length - 1));

        if (length == 3 && integer.get() == 3)
            textField.setValue(textField.getValue().substring(0, length - 1) + ":" + textField.getValue().charAt(length - 1));

        try {
            if (length != 5) throw new ValidationException("Godzina musi zawierać 4 cyfry");
            else toHour.focus();

            if (!textField.getValue().matches("(?:[0-1][0-9]|2[0-4]):[0-5]\\d")) {
                throw new ValidationException("Wartość zawiera niezgodne znaki");
            }

            textField.setInvalid(false);
        } catch (ValidationException ex) {
            textField.setErrorMessage(ex.getMessage());
            textField.setInvalid(true);
        }

        integer.set(length);
    }
}