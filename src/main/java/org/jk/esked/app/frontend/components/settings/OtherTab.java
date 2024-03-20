package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.backend.model.types.ThemeType;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.other.SuccessNotification;
import org.jk.esked.app.frontend.components.fields.ScheduleHoursSetter;

import java.util.UUID;

public class OtherTab extends SettingsTab {
    public OtherTab(UUID userId, UserService userService, HourService hourService) {
        super(SettingsTabType.OTHER);

        RadioButtonGroup<String> scheduleHours = new RadioButtonGroup<>();
        scheduleHours.setLabel(getTranslation("schedule.hours"));
        scheduleHours.setItems(getTranslation("yes"), getTranslation("no"));
        if (userService.isHourEnabled(userId)) scheduleHours.setValue(getTranslation("yes"));
        else scheduleHours.setValue(getTranslation("no"));
        scheduleHours.addValueChangeListener(valueChange -> userService.changeHourByUserId(userId, valueChange.getValue().equals(getTranslation("yes"))));

        Details setHours = new Details(getTranslation("schedule.set.hours"), new ScheduleHoursSetter(userId, hourService));
        setHours.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        if (hourService.getScheduleMaxHour(userId) == 0) setHours.setEnabled(false);

        RadioButtonGroup<String> theme = new RadioButtonGroup<>();
        theme.setLabel(getTranslation("schedule.theme"));
        theme.setItems(getTranslation("schedule.theme.light"), getTranslation("schedule.theme.dark"));
        if (userService.getThemeByUserId(userId) == ThemeType.DARK)
            theme.setValue(getTranslation("schedule.theme.dark"));
        else theme.setValue(getTranslation("schedule.theme.light"));
        theme.addValueChangeListener(valueChange -> {
            if (valueChange.getValue().equals(getTranslation("schedule.theme.dark")))
                userService.changeThemeByUserId(userId, ThemeType.DARK);
            else
                userService.changeThemeByUserId(userId, ThemeType.WHITE);
        });

        Button button = new Button(getTranslation("schedule.delete.hours"), click -> {
            hourService.deleteAllHourByUserId(userId);
            new SuccessNotification(getTranslation("notification.hours.deleted"), NotificationType.SHORT);
        });

        FormLayout otherForm = new FormLayout(scheduleHours, setHours, theme, button);
        add(otherForm);
    }
}
