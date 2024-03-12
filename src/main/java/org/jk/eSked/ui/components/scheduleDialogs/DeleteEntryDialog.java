package org.jk.eSked.ui.components.scheduleDialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.ScheduleService;

import java.util.Locale;

public class DeleteEntryDialog extends Dialog {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();

    public DeleteEntryDialog(ScheduleService scheduleService, ScheduleEntry entry) {
        Label label = new Label(getTranslation(locale, "schedule_dialog_confirmation"));

        Button deleteButton = new Button(getTranslation(locale, "delete"), event -> { //TODO translation
            scheduleService.deleteScheduleEntry(SessionService.getUserId(), entry.getHour(), entry.getDay());
            close();
        });

        deleteButton.getStyle().set("color", "red");
        deleteButton.setWidth("10vw");

        VerticalLayout layout = new VerticalLayout(label, deleteButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        setWidth("30vw");
        add(layout);
    }
}
