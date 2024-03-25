package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.frontend.components.buttons.RedButton;

public class DeleteHourDialog extends Dialog {
    public DeleteHourDialog(HourService hourService, Button source, User user, int hour) {
        Button delete = new RedButton(getTranslation("delete"), s -> {
            hourService.deleteHourByUserIdAndHour(user.getId(), hour);
            source.setText(String.valueOf(hour));
            close();
        });
        addClassName("delete-dialog");
        add(delete);
        open();
    }
}
