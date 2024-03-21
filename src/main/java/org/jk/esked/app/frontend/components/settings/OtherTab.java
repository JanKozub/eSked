package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.backend.model.types.ThemeType;
import org.jk.esked.app.backend.services.UserService;

import java.util.UUID;

public class OtherTab extends SettingsTab {
    public OtherTab(UUID userId, UserService userService) {
        super(SettingsTabType.OTHER);

        RadioButtonGroup<String> scheduleHours = new RadioButtonGroup<>();
        scheduleHours.setLabel(getTranslation("schedule.hours"));
        scheduleHours.setItems(getTranslation("yes"), getTranslation("no"));
        if (userService.isHourEnabled(userId)) scheduleHours.setValue(getTranslation("yes"));
        else scheduleHours.setValue(getTranslation("no"));
        scheduleHours.addValueChangeListener(valueChange -> userService.changeHourById(userId, valueChange.getValue().equals(getTranslation("yes"))));

        RadioButtonGroup<String> theme = new RadioButtonGroup<>();
        theme.setLabel(getTranslation("schedule.theme"));
        theme.setItems(getTranslation("schedule.theme.light"), getTranslation("schedule.theme.dark"));
        if (userService.findThemeById(userId) == ThemeType.DARK)
            theme.setValue(getTranslation("schedule.theme.dark"));
        else theme.setValue(getTranslation("schedule.theme.light"));
        theme.addValueChangeListener(valueChange -> {
            if (valueChange.getValue().equals(getTranslation("schedule.theme.dark")))
                userService.changeThemeById(userId, ThemeType.DARK);
            else
                userService.changeThemeById(userId, ThemeType.WHITE);
        });

        add(new FormLayout(scheduleHours, theme));
    }
}
