package org.jk.eSked.ui.components.scheduleDialogs;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.ScheduleService;

public class DeleteEntryDialog extends Dialog {
    public DeleteEntryDialog(ScheduleService scheduleService, ScheduleEntry entry) {
        Text label = new Text(getTranslation("schedule.dialog.confirmation"));

        Button deleteButton = new Button(getTranslation("delete"), event -> {
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
