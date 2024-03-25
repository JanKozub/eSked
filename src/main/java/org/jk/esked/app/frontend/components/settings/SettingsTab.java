package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.frontend.components.other.HorizontalLine;

abstract class SettingsTab extends VerticalLayout {
    public SettingsTab(SettingsTabType settingsTabType) {
        Span label = new Span(getTranslation("settings.tab." + settingsTabType.getDescription()));
        add(label, new HorizontalLine());
        addClassName("settings-tab");
    }
}