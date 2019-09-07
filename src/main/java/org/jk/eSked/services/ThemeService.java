package org.jk.eSked.services;

import com.vaadin.flow.component.UI;

import java.time.LocalDateTime;

public class ThemeService {
    public ThemeService() {
    }

    public void check() {
        LocalDateTime currentTime = LocalDateTime.now();
        int hour = currentTime.getHour() - 4;
        if (hour > 15 || hour <= 0) {
            UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"dark\")");
        } else {
            UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"white\")");
        }
    }
}
