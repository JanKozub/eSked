package org.jk.eSked.ui.views.events;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.ui.components.menu.Menu;
import org.jk.eSked.ui.components.schedule.EventGrid;

@Route(value = "events", layout = Menu.class)
@PageTitle("Wydarzenia")
public class EventsView extends VerticalLayout {

    private EventService eventService;

    public EventsView(ScheduleService scheduleService, EventService eventService) {
        this.eventService = eventService;
        SessionService.setAutoTheme();
        VerticalLayout eventGrid = new EventGrid(scheduleService, eventService, SessionService.getUserId());
        Button newEventButton = new Button("Dodaj nowe wydarzenie", e -> UI.getCurrent().navigate("events/new"));
        newEventButton.setWidth("100%");
        add(eventGrid, newEventButton);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        Menu.setEventsBadge(eventService.getUncheckedEvents(SessionService.getUserId()).size());
    }
}
