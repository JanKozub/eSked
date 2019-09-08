package org.jk.eSked.view.exceptions;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@SuppressWarnings("unused")
@Route(value = "login")
class NoPathView extends VerticalLayout {

    public NoPathView() {
        UI.getCurrent().navigate("");
        UI.getCurrent().getPage().reload();
    }
}