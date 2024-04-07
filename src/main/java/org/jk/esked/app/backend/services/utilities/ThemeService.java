package org.jk.esked.app.backend.services.utilities;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.WebStorage;

public class ThemeService {
    public static void setTheme(String theme) {
        WebStorage.setItem(WebStorage.Storage.SESSION_STORAGE, "theme", theme);
        refreshTheme();
    }

    public static void refreshTheme() {
        WebStorage.getItem(WebStorage.Storage.SESSION_STORAGE, "theme", value -> {
            if (value != null)
                UI.getCurrent().getElement().setAttribute("theme", value);
        });
    }
}
