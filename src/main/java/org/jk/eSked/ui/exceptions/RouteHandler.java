package org.jk.eSked.ui.exceptions;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

@Tag(Tag.DIV)
class RouteHandler extends Component implements HasErrorParameter<NotFoundException> {
    private static final Logger log = LoggerFactory.getLogger(RouteHandler.class);

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        UI.getCurrent().navigate("login");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}