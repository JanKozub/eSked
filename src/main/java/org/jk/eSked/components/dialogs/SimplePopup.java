package org.jk.eSked.components.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SimplePopup {
    public void open(String msg) {
        Dialog dialog = new Dialog();
        Label label = new Label(msg);
        Button button = new Button("Zamknij", event -> dialog.close());
        button.setSizeFull();
        VerticalLayout layout = new VerticalLayout(label, button);
        dialog.add(layout);
        dialog.open();
    }
}
