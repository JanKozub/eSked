package org.jk.eSked.ui.components.myComponents;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyDatePicker extends DatePicker {
    private final Locale locale;

    public MyDatePicker() {
        locale = VaadinSession.getCurrent().getLocale();

        setI18n(new DatePicker.DatePickerI18n()
                .setWeek(getTranslation(locale, "calendar_week"))
                .setCalendar(getTranslation(locale, "calendar_calendar"))
                .setClear(getTranslation(locale, "calendar_clear"))
                .setToday(getTranslation(locale, "calendar_today"))
                .setCancel(getTranslation(locale, "calendar_cancel"))
                .setFirstDayOfWeek(1)
                .setMonthNames(getMonthsNames())
                .setWeekdays(getDaysNames())
                .setWeekdaysShort(getShortDaysNames()));
        setWidth("25%");
    }

    private List<String> getMonthsNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) list.add(getTranslation(locale, "calendar_month_" + i));

        return list;
    }

    private List<String> getDaysNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) list.add(getTranslation(locale, "calendar_week_" + i));

        return list;
    }

    private List<String> getShortDaysNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) list.add(getTranslation(locale, "calendar_s_" + i));

        return list;
    }
}
