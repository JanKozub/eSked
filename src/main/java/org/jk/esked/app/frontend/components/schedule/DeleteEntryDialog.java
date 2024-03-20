package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.jk.esked.app.backend.services.ScheduleService;

import java.util.UUID;

public class DeleteEntryDialog extends Dialog {
    public DeleteEntryDialog(UUID userId, ScheduleService scheduleService, ScheduleEntry entry) {
        Text label = new Text(getTranslation("schedule.dialog.confirmation"));

        Button deleteButton = new Button(getTranslation("delete"), event -> {
            scheduleService.deleteScheduleEntry(userId, entry.getHour(), entry.getDay());
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
