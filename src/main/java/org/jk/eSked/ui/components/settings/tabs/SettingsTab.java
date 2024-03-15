package org.jk.eSked.ui.components.settings.tabs;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.eSked.ui.components.myComponents.Line;

abstract class SettingsTab extends VerticalLayout {
    public SettingsTab(Text label) {
        add(label, new Line());
        getStyle().set("flex-shrink", "0");
        getStyle().set("padding-top", "8px");
    }
}