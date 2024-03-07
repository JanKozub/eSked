package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.jk.eSked.ui.components.myComponents.MyDatePicker;

import java.time.LocalDate;

abstract class DatePanel extends HorizontalLayout {
    private static final boolean NEXT_WEEK = true;
    private static final boolean PREVIOUS_WEEK = false;
    private static MyDatePicker dateFrom;
    private static MyDatePicker dateTo;

    DatePanel(LocalDate startOfWeek) {
        Button prevWeek = new Button(new Icon(VaadinIcon.ARROW_LEFT));
        prevWeek.addClickListener(e -> refreshDates(changeWeek(NEXT_WEEK)));

        dateFrom = new MyDatePicker() {
            @Override
            protected void setWeekForDay(LocalDate day) {
                DatePanel.this.setWeekForDay(day);
            }
        };

        Icon arrowIcon = new Icon(VaadinIcon.ARROW_RIGHT);

        dateTo = new MyDatePicker() {
            @Override
            protected void setWeekForDay(LocalDate day) {
                DatePanel.this.setWeekForDay(day);
            }
        };
        dateTo.setEnabled(false);

        Button nextWeek = new Button(new Icon(VaadinIcon.ARROW_RIGHT));
        nextWeek.addClickListener(f -> refreshDates(changeWeek(PREVIOUS_WEEK)));

        add(prevWeek, dateFrom, arrowIcon, dateTo, nextWeek);
        setWidth("100%");
        prevWeek.setWidth("20%");
        arrowIcon.getStyle().set("width", "10%");
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
