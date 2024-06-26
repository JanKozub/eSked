package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.theme.lumo.Lumo;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.backend.model.types.ThemeType;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.backend.services.utilities.ThemeService;
import org.jk.esked.app.frontend.components.buttons.RedButton;

import java.util.UUID;

public class OtherTab extends SettingsTab {
    public OtherTab(UUID userId, UserService userService, SecurityService securityService) {
        super(SettingsTabType.OTHER);
        ThemeService.refreshTheme();

        SettingsRadioGroup scheduleHours = new SettingsRadioGroup("schedule.hours",
                "yes", "no", userService.isHourEnabled(userId));
        scheduleHours.addValueChangeListener(valueChange ->
                userService.changeHourById(userId, valueChange.getValue().equals(getTranslation("yes"))));

        SettingsRadioGroup theme = new SettingsRadioGroup("schedule.theme", "schedule.theme.light",
                "schedule.theme.dark", userService.findThemeById(userId) == ThemeType.WHITE);
        theme.addValueChangeListener(v -> {
            ThemeType themeType = v.getValue().equals(getTranslation("schedule.theme.dark")) ? ThemeType.DARK : ThemeType.WHITE;
            userService.changeThemeById(userId, themeType);
            ThemeService.setTheme(themeType == ThemeType.DARK ? Lumo.DARK : Lumo.LIGHT);
        });

        Button deleteButton = new RedButton(getTranslation("settings.tab.delete"), c -> {
            Button button = new RedButton(getTranslation("confirm"), e -> {
                securityService.logout();
                userService.deleteUser(userId);
            });
            button.setWidth("100%");
            new Dialog(button).open();
        });

        addClassName("other-tab");
        add(new FormLayout(scheduleHours, theme), deleteButton);
    }
}
