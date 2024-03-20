package org.jk.esked.app.frontend.components.fields;

import com.vaadin.flow.component.timepicker.TimePicker;

import java.time.Duration;

public class TimePicker24h extends TimePicker {
    public TimePicker24h() {
        setStep(Duration.ofMinutes(15));
    }

    public String getValueIn24h() {
        int minute = getValue().getMinute();
        return getValue().getHour() + ":" + (minute < 9 ? "0" + minute : String.valueOf(minute));
    }
}
