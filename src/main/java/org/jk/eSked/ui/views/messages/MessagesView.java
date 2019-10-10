package org.jk.eSked.ui.views.messages;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.ui.components.menu.Menu;

@Route(value = "messages", layout = Menu.class)
@PageTitle("Wiadomości")
public class MessagesView extends VerticalLayout {

    public MessagesView() {

    }
}
