package org.jk.esked.app.frontend.components.settings.tabs;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.esked.app.frontend.components.Line;

abstract class SettingsTab extends VerticalLayout {
    public SettingsTab(Text label) {
        add(label, new Line());
        getStyle().set("flex-shrink", "0");
        getStyle().set("padding-top", "8px");
    }
}