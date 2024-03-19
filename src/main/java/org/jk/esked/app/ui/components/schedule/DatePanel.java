package org.jk.esked.app.ui.components.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.jk.esked.app.ui.components.MyDatePicker;

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
        dateTo.setWidth("25%");

        Button prevWeek = new Button(new Icon(VaadinIcon.ARROW_LEFT));
        prevWeek.addClickListener(e -> refreshDates(changeWeek(NEXT_WEEK), dateFrom, dateTo));

        Icon arrowIcon = new Icon(VaadinIcon.ARROW_RIGHT);

        Button nextWeek = new Button(new Icon(VaadinIcon.ARROW_RIGHT));
        nextWeek.addClickListener(f -> refreshDates(changeWeek(PREVIOUS_WEEK), dateFrom, dateTo));

        add(prevWeek, dateFrom, arrowIcon, dateTo, nextWeek);
        setWidth("100%");
        prevWeek.setWidth("20%");
        arrowIcon.getStyle().set("width", "10%");
        nextWeek.setWidth("20%");
        setDefaultVerticalComponentAlignment(Alignment.CENTER);

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
