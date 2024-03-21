package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.esked.app.backend.model.entities.Hour;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.frontend.components.fields.TimePicker24h;

public class AddHourDialog extends Dialog {
    public AddHourDialog(HourService hourService, Button source, User user, int hour) {
        setCloseOnOutsideClick(true);

        TimePicker24h timePicker = new TimePicker24h();
        Button submit = new Button(getTranslation("add"), s -> {
            if (hourService.getHourValueByHour(user.getId(), hour) != null) return;

            hourService.saveHour(new Hour(user, hour, timePicker.getValueIn24h()));
            source.setText(timePicker.getValueIn24h());
            close();
        });
        submit.setWidth("100%");
        add(new VerticalLayout(timePicker, submit));
        open();
    }
}
