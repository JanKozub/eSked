package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.jk.esked.app.frontend.components.other.MyDatePicker;

import java.time.DayOfWeek;
import java.time.LocalDate;

public abstract class DatePanel extends HorizontalLayout {
    private static final boolean NEXT_WEEK = true;
    private static final boolean PREVIOUS_WEEK = false;
    private LocalDate startOfWeek;

    protected DatePanel(LocalDate startOfWeek) {
        this.startOfWeek = startOfWeek;

        MyDatePicker dateFrom = new MyDatePicker();
        dateFrom.addValueChangeListener(event -> setWeekForDay(event.getValue()));

        DatePicker dateTo = new DatePicker();
        dateTo.setEnabled(false);

        Button prevWeek = new Button(new Icon(VaadinIcon.ARROW_LEFT),
                e -> refreshDates(changeWeek(NEXT_WEEK), dateFrom, dateTo));

        Icon arrowIcon = new Icon(VaadinIcon.ARROW_RIGHT);

        Button nextWeek = new Button(new Icon(VaadinIcon.ARROW_RIGHT),
                e -> refreshDates(changeWeek(PREVIOUS_WEEK), dateFrom, dateTo));

        addClassName("date-panel");
        add(prevWeek, dateFrom, arrowIcon, dateTo, nextWeek);
        refreshDates(startOfWeek, dateFrom, dateTo);
    }

    private void refreshDates(LocalDate startOfWeek, MyDatePicker dateFrom, DatePicker dateTo) {
        dateFrom.setValue(startOfWeek);
        dateTo.setValue(startOfWeek.plusDays(6));
    }

    private LocalDate changeWeek(boolean weekType) {
        int deltaDays = (weekType) ? -7 : 7;
        startOfWeek = startOfWeek.plusDays(deltaDays).with(DayOfWeek.MONDAY);
        return startOfWeek;
    }

    public abstract void setWeekForDay(LocalDate day);
}
