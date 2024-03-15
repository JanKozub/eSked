package org.jk.eSked.ui.exceptions;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@Tag(Tag.DIV)
class RouteHandler extends Component implements HasErrorParameter<NotFoundException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        UI.getCurrent().navigate("login");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}