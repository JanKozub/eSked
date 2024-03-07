package org.jk.eSked.ui.components.myComponents;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.server.VaadinSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class MyDatePicker extends DatePicker {
    private Locale locale;

    public MyDatePicker() {
        locale = VaadinSession.getCurrent().getLocale();

        setI18n(new DatePicker.DatePickerI18n()
                .setWeek(getTranslation("calendar_week", locale))
                .setCalendar(getTranslation("calendar_calendar", locale))
                .setClear(getTranslation("calendar_clear", locale))
                .setToday(getTranslation("calendar_today", locale))
                .setCancel(getTranslation("calendar_cancel", locale))
                .setFirstDayOfWeek(1)
                .setMonthNames(getMonthsNames())
                .setWeekdays(getDaysNames())
                .setWeekdaysShort(getShortDaysNames()));
        addValueChangeListener(event -> setWeekForDay(event.getValue()));
        setWidth("25%");
    }

    private List<String> getMonthsNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) list.add(getTranslation("calendar_month_" + i, locale));

        return list;
    }

    private List<String> getDaysNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) list.add(getTranslation("calendar_week_" + i, locale));

        return list;
    }

    private List<String> getShortDaysNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) list.add(getTranslation("calendar_s_" + i, locale));

        return list;
    }

    protected abstract void setWeekForDay(LocalDate day);
}
