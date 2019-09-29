package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.time.LocalDate;
import java.util.Arrays;

abstract class DatePanel extends HorizontalLayout {
    private static final boolean NEXT_WEEK = true;
    private static final boolean PREVIOUS_WEEK = false;
    private static DatePicker dateFrom;
    private static DatePicker dateTo;

    DatePanel(LocalDate startOfWeek) {
        Button prevWeek = new Button(new Icon(VaadinIcon.ARROW_LEFT));
        prevWeek.addClickListener(e -> refreshDates(changeWeek(NEXT_WEEK)));

        dateFrom = new DatePicker();
        dateFrom.setI18n(new DatePicker.DatePickerI18n().setWeek("tydzień").setCalendar("kalendarz")
                .setClear("wyczyść").setToday("dzisiaj")
                .setCancel("zamknij").setFirstDayOfWeek(1)
                .setMonthNames(Arrays.asList("styczeń", "luty", "marzec", "kwiecień", "maj", "czerwiec", "lipiec", "sierpień", "wrzesień", "październik", "listopad", "grudzień"))
                .setWeekdays(Arrays.asList("niedziela", "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota"))
                .setWeekdaysShort(Arrays.asList("niedź", "pon", "wt", "śr", "czw", "pt", "sob")));
        dateFrom.addValueChangeListener(event -> setWeekForDay(event.getValue()));

        Icon arrowIcon = new Icon(VaadinIcon.ARROW_RIGHT);

        dateTo = new DatePicker();
        dateTo.setEnabled(false);

        Button nextWeek = new Button(new Icon(VaadinIcon.ARROW_RIGHT));
        nextWeek.addClickListener(f -> refreshDates(changeWeek(PREVIOUS_WEEK)));

        add(prevWeek, dateFrom, arrowIcon, dateTo, nextWeek);
        setWidth("100%");
        prevWeek.setWidth("20%");
        dateFrom.setWidth("25%");
        arrowIcon.getStyle().set("width", "10%");
        dateTo.setWidth("25%");
        nextWeek.setWidth("20%");
        setDefaultVerticalComponentAlignment(Alignment.CENTER);

        refreshDates(startOfWeek);
    }

    private void refreshDates(LocalDate startOfWeek) {
        dateFrom.setValue(startOfWeek);
        dateTo.setValue(startOfWeek.plusDays(6));
    }

    abstract void setWeekForDay(LocalDate day);

    abstract LocalDate changeWeek(boolean weekType);
}
