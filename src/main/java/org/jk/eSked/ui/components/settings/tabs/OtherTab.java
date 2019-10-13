package org.jk.eSked.ui.components.settings.tabs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.model.types.ThemeType;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.jk.eSked.ui.components.settings.fields.ScheduleHoursSetter;

public class OtherTab extends SettingsTab {

    public OtherTab() {
        super(new Label("Inne"));

        RadioButtonGroup<String> scheduleHours = new RadioButtonGroup<>();
        scheduleHours.setLabel("godziny w planie");
        scheduleHours.setItems("Tak", "Nie");
        if (userService.getScheduleHours(userId)) scheduleHours.setValue("Tak");
        else scheduleHours.setValue("Nie");
        scheduleHours.addValueChangeListener(valueChange -> userService.setScheduleHours(userId, valueChange.getValue().equals("Tak")));

        Details setHours = new Details("Ustaw godziny", new ScheduleHoursSetter(userId, hoursService));
        setHours.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        if (hoursService.getScheduleMaxHour(userId) == 0) setHours.setEnabled(false);

        RadioButtonGroup<String> theme = new RadioButtonGroup<>();
        theme.setLabel("Styl strony");
        theme.setItems("Jasny", "Ciemny");
        if (userService.getTheme(userId) == ThemeType.DARK) theme.setValue("Ciemny");
        else theme.setValue("Jasny");
        theme.addValueChangeListener(valueChange -> {
            boolean mode = valueChange.getValue().equals("Ciemny");
            if (mode) userService.setTheme(userId, ThemeType.DARK);
            else userService.setTheme(userId, ThemeType.WHITE);
            SessionService.setAutoTheme();
        });

        Button button = new Button("Usuń aktualne godziny", click -> {
            hoursService.deleteScheduleHours(userId);
            new SuccessNotification("Usunięto aktualne godziny", NotificationType.SHORT);
        });

        FormLayout otherForm = new FormLayout(scheduleHours, setHours, theme, button);
        add(otherForm);
    }
}
