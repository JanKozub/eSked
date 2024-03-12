package org.jk.eSked.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.ui.components.events.EventGrid;
import org.jk.eSked.ui.components.schedule.DatePanel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

@Route(value = "events", layout = MainLayout.class)
public class EventsView extends VerticalLayout implements HasDynamicTitle {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
    private final EventGrid eventGrid;

    public EventsView(ScheduleService scheduleService, EventService eventService) {
        eventGrid = new EventGrid(scheduleService, eventService, startOfWeek);

        HorizontalLayout datePanel = new DatePanel(startOfWeek) {
            @Override
            public void setWeekForDay(LocalDate day) {
                EventsView.this.setWeekForDay(day);
            }
        };

        Button newEventButton = new Button(getTranslation(VaadinSession.getCurrent()
                .getLocale(), "events_add_new_event"), e -> UI.getCurrent().navigate("events/new"));
        newEventButton.setWidth("100%");

        add(datePanel, eventGrid, newEventButton);
    }

    private void setWeekForDay(LocalDate day) {
        day = (LocalDate) TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY).adjustInto(day);
        startOfWeek = day;
        eventGrid.reloadForWeek(startOfWeek);
    }

    @Override
    public String getPageTitle() {
        return getTranslation(locale, "page_events");
    }
}
