package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.HourService;

public class DeleteHourDialog extends Dialog {
    public DeleteHourDialog(HourService hourService, Button source, User user, int hour) {
        Button delete = new Button(getTranslation("delete"), s -> {
            hourService.deleteHourByUserIdAndHour(user.getId(), hour);

            source.setText(String.valueOf(hour));
            close();
        });
        delete.setWidth("100%");
        add(delete);
        open();
    }
}
