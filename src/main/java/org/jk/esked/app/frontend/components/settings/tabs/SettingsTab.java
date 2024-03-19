package org.jk.esked.app.frontend.components.settings.tabs;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.frontend.components.Line;

abstract class SettingsTab extends VerticalLayout {
    public SettingsTab(SettingsTabType settingsTabType) {
        Span label = new Span(getTranslation("settings.tab." + settingsTabType.getDescription()));
        label.addClassName("centered-text");
        add(label, new Line());
        getStyle().set("flex-shrink", "0");
        getStyle().set("padding-top", "8px");
    }
}