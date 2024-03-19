package org.jk.esked.app.frontend.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.RouterLink;

public class DrawerTab extends Tab {
    public DrawerTab(VaadinIcon icon, String title, Class<? extends Component> routerClass) {
        super(icon.create(), new RouterLink(title, routerClass));
    }
}
