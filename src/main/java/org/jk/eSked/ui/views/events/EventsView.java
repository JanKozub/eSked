package org.jk.eSked.ui.views.events;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.EventService;
import org.jk.eSked.backend.service.ScheduleService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.schedule.EventGrid;

@Route(value = "events", layout = MenuView.class)
@PageTitle("Wydarzenia")
public class EventsView extends VerticalLayout {

    public EventsView(ScheduleService scheduleService, EventService eventService) {
        VerticalLayout eventGrid = new EventGrid(scheduleService, eventService, SessionService.getUserId());
        Button newEventButton = new Button("Dodaj nowe wydarzenie", e -> UI.getCurrent().navigate("events/new"));
        newEventButton.setWidth("100%");
        add(eventGrid, newEventButton);
    }
}