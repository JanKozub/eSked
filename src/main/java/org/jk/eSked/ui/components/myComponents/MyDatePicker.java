package org.jk.eSked.ui.components.myComponents;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyDatePicker extends DatePicker {//TODO use getProperties instead of for loops
    private final Locale locale;

    public MyDatePicker() {
        locale = VaadinSession.getCurrent().getLocale();

        setI18n(new DatePicker.DatePickerI18n()
                .setWeek(getTranslation(locale, "calendar.week"))
                .setToday(getTranslation(locale, "calendar.today"))
                .setCancel(getTranslation(locale, "calendar.cancel"))
                .setFirstDayOfWeek(1)
                .setMonthNames(getMonthsNames())
                .setWeekdays(getDaysNames())
                .setWeekdaysShort(getShortDaysNames()));
        setWidth("25%");
    }

    private List<String> getMonthsNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) list.add(getTranslation(locale, "calendar.month." + i));

        return list;
    }

    private List<String> getDaysNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) list.add(getTranslation(locale, "day." + i));

        return list;
    }

    private List<String> getShortDaysNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) list.add(getTranslation(locale, "calendar.s." + i));

        return list;
    }
}
