package org.jk.eSked.ui.components.settings.tabs;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.model.types.ThemeType;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.HoursService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.jk.eSked.ui.components.settings.fields.ScheduleHoursSetter;

import java.util.UUID;

public class OtherTab extends SettingsTab {
    public OtherTab(UserService userService, HoursService hoursService, String title) {
        super(new Text(title));
        UUID userId = SessionService.getUserId();

        RadioButtonGroup<String> scheduleHours = new RadioButtonGroup<>();
        scheduleHours.setLabel(getTranslation("schedule.hours"));
        scheduleHours.setItems(getTranslation("yes"), getTranslation("no"));
        if (userService.getScheduleHours(userId)) scheduleHours.setValue(getTranslation("yes"));
        else scheduleHours.setValue(getTranslation("no"));
        scheduleHours.addValueChangeListener(valueChange -> userService.setScheduleHours(userId, valueChange.getValue().equals(getTranslation("yes"))));

        Details setHours = new Details(getTranslation("schedule.set.hours"), new ScheduleHoursSetter(userId, hoursService));
        setHours.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        if (hoursService.getScheduleMaxHour(userId) == 0) setHours.setEnabled(false);

        RadioButtonGroup<String> theme = new RadioButtonGroup<>();
        theme.setLabel(getTranslation("schedule.theme"));
        theme.setItems(getTranslation("schedule.theme.light"), getTranslation("schedule.theme.dark"));
        if (userService.getTheme(userId) == ThemeType.DARK) theme.setValue(getTranslation("schedule.theme.dark"));
        else theme.setValue(getTranslation("schedule.theme.light"));
        theme.addValueChangeListener(valueChange -> {
            if (valueChange.getValue().equals(getTranslation("schedule.theme.dark")))
                userService.setTheme(userId, ThemeType.DARK);
            else
                userService.setTheme(userId, ThemeType.WHITE);

            SessionService.setAutoTheme();
        });

        Button button = new Button(getTranslation("schedule.delete.hours"), click -> {
            hoursService.deleteScheduleHours(userId);
            new SuccessNotification(getTranslation("notification.hours.deleted"), NotificationType.SHORT);
        });

        FormLayout otherForm = new FormLayout(scheduleHours, setHours, theme, button);
        add(otherForm);
    }
}
