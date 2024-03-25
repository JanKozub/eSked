package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.frontend.components.buttons.RedButton;

import java.util.UUID;

public class DeleteEntryDialog extends Dialog {
    public DeleteEntryDialog(UUID userId, ScheduleEntryService scheduleEntryService, ScheduleEntry entry) {
        Button deleteButton = new RedButton(getTranslation("delete"), event -> {
            scheduleEntryService.delete(userId, entry.getHour(), entry.getDay());
            close();
        });

        addClassName("delete-dialog");
        add(new VerticalLayout(new Span(getTranslation("schedule.dialog.confirmation")), deleteButton));
        open();
    }
}
