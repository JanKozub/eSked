package org.jk.eSked.backend.service;

import com.vaadin.flow.component.UI;
import org.jk.eSked.backend.model.types.ThemeType;

public class ThemeService {
    public static void setTheme(ThemeType themeType) {
        UI.getCurrent().getPage()
                .executeJs("document.documentElement.setAttribute(\"theme\",\"" + themeType.toString().toLowerCase() + "\")");
    }
}
