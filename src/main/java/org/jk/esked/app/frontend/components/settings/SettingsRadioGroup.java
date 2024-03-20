package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

public class SettingsRadioGroup extends RadioButtonGroup<String> {
    public SettingsRadioGroup(String labelKey, String option1Key, String option2Key, boolean initState) {
        setLabel(getTranslation(labelKey));
        setItems(getTranslation(option1Key), getTranslation(option2Key));
        if (initState) setValue(getTranslation(option1Key));
        else setValue(getTranslation(option2Key));
    }
}
