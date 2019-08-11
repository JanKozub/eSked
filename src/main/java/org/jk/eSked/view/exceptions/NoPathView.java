package org.jk.eSked.view.exceptions;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@SuppressWarnings("unused")
@Route(value = "")
class NoPathView extends VerticalLayout {

    public NoPathView() {
        UI.getCurrent().navigate("login");
        UI.getCurrent().getPage().reload();
    }
}