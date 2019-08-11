package org.jk.eSked.component;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class InfoDialog extends Dialog {
    public InfoDialog() {
        Label name = new Label("O Aplikacji");
        name.getStyle().set("font-weight", "bold");

        VerticalLayout nameLayout = new VerticalLayout(name);
        nameLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        Label text = new Label("[...]");

        add(nameLayout, text);
    }
}
