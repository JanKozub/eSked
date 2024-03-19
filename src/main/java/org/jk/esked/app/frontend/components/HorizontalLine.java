package org.jk.esked.app.frontend.components;

import com.vaadin.flow.component.html.Div;

public class HorizontalLine extends Div {
    public HorizontalLine() {
        getStyle().set("background-color", "#d8e1ed");
        setWidth("100%");
        setHeight("3px");
    }
}
