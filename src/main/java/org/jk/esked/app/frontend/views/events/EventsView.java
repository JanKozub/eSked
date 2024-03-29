package org.jk.esked.app.frontend.views.events;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.EventService;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.frontend.components.buttons.WideButton;
import org.jk.esked.app.frontend.components.events.EventGrid;
import org.jk.esked.app.frontend.components.schedule.DatePanel;
import org.jk.esked.app.frontend.views.MainLayout;
import org.springframework.context.annotation.Scope;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@SpringComponent
@PermitAll
@Scope("prototype")
@Route(value = "events", layout = MainLayout.class)
public class EventsView extends VerticalLayout implements HasDynamicTitle {
    private final EventGrid eventGrid;
    private LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

    public EventsView(ScheduleEntryService scheduleEntryService, EventService eventService, SecurityService securityService) {
        eventGrid = new EventGrid(securityService.getUserId(), scheduleEntryService, eventService, startOfWeek);

        HorizontalLayout datePanel = new DatePanel(startOfWeek) {
            @Override
            public void setWeekForDay(LocalDate day) {
                EventsView.this.setWeekForDay(day);
            }
        };

        Button newEventButton = new WideButton(getTranslation("events.add.new.event"), e -> UI.getCurrent().navigate("events/new"));

        add(datePanel, eventGrid, newEventButton);
    }

    private void setWeekForDay(LocalDate day) {
        day = (LocalDate) TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY).adjustInto(day);
        startOfWeek = day;
        eventGrid.setStartOfWeek(day);
        eventGrid.reloadForWeek();
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.events");
    }
}
