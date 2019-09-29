package org.jk.eSked.ui.views.events;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.app.LoginService;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.EventService;
import org.jk.eSked.backend.service.ScheduleService;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.schedule.EventGrid;

@Route(value = "events", layout = MenuView.class)
@PageTitle("Wydarzenia")
public class EventsView extends VerticalLayout {

    public EventsView(LoginService loginService, ScheduleService scheduleService, EventService eventService) {

        if (loginService.checkIfUserIsLogged()) {
            VerticalLayout eventGrid = new EventGrid(scheduleService, eventService, VaadinSession.getCurrent().getAttribute(User.class).getId());
            Button newEventButton = new Button("Dodaj nowe wydarzenie", e -> UI.getCurrent().navigate("events/new"));
            newEventButton.setWidth("100%");
            add(eventGrid, newEventButton);
        }
    }
}