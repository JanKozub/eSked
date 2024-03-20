package org.jk.esked.app.frontend.exceptions;

import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.RouteAccessDeniedError;
import com.vaadin.flow.server.HttpStatusCode;
import org.jk.esked.app.frontend.views.schedule.ScheduleView;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedError extends RouteAccessDeniedError {
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<AccessDeniedException> parameter) {
        event.forwardTo(ScheduleView.class);
        return HttpStatusCode.UNAUTHORIZED.getCode();
    }
}