package org.jk.esked.app.frontend.components;

import com.vaadin.flow.component.html.Div;

public class VerticalLine extends Div {
    public VerticalLine() {
        getStyle().set("background-color", "#d8e1ed");
        setWidth("3px");
        setHeight("415px"); //TODO relative
    }
}
