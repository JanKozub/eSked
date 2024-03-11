package org.jk.eSked.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.ui.components.event.EventGrid;
import org.jk.eSked.ui.components.schedule.DatePanel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Route(value = "events", layout = MainLayout.class)
@PageTitle("Wydarzenia")
public class EventsView extends VerticalLayout {
    private final EventGrid eventGrid;
    private LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

    public EventsView(ScheduleService scheduleService, EventService eventService) {
        eventGrid = new EventGrid(scheduleService, eventService, startOfWeek);

        HorizontalLayout datePanel = new DatePanel(startOfWeek) {
            @Override
            public void setWeekForDay(LocalDate day) {
                EventsView.this.setWeekForDay(day);
            }
        };

        Button newEventButton = new Button("Dodaj nowe wydarzenie", e -> UI.getCurrent().navigate("events/new"));
        newEventButton.setWidth("100%");

        add(datePanel, eventGrid, newEventButton);
    }

    private void setWeekForDay(LocalDate day) {
        day = (LocalDate) TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY).adjustInto(day);
        startOfWeek = day;
        eventGrid.reloadForWeek(startOfWeek);
    }
}
