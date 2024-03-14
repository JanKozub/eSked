package org.jk.eSked.ui.components.myComponents;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.util.ArrayList;
import java.util.List;

public class MyDatePicker extends DatePicker {
    public MyDatePicker() {
        setI18n(new DatePicker.DatePickerI18n()
                .setWeek(getTranslation("calendar.week"))
                .setToday(getTranslation("calendar.today"))
                .setCancel(getTranslation("calendar.cancel"))
                .setFirstDayOfWeek(1)
                .setMonthNames(getMonthsNames())
                .setWeekdays(getDaysNames())
                .setWeekdaysShort(getShortDaysNames()));
        setWidth("25%");
    }

    private List<String> getMonthsNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) list.add(getTranslation("calendar.month." + i));

        return list;
    }

    private List<String> getDaysNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) list.add(getTranslation("day." + i));

        return list;
    }

    private List<String> getShortDaysNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) list.add(getTranslation("calendar.s." + i));

        return list;
    }
}
