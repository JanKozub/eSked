package org.jk.eSked.ui.views.messages;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.MessagesService;
import org.jk.eSked.ui.components.menu.Menu;
import org.jk.eSked.ui.components.myComponents.Line;
import org.jk.eSked.ui.components.myComponents.MessageBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Route(value = "messages", layout = Menu.class)
@PageTitle("Wiadomości")
public class MessagesView extends VerticalLayout {

    private MessagesService messagesService;

    public MessagesView(MessagesService messagesService) {
        this.messagesService = messagesService;
        SessionService.setAutoTheme();

        Label title = new Label("Wiadomości");
        Line line = new Line();
        line.setWidth("50%");
        add(title, line);

        renderMessages();

        setAlignItems(Alignment.CENTER);
        setSizeFull();
    }

    private void renderMessages() {
        List<Message> messages = new ArrayList<>(messagesService.getMessagesForUser(SessionService.getUserId()));
        messages.sort(Comparator.comparing(sortedMessage -> TimeService.InstantToLocalDate(sortedMessage.getTimestamp())));
        for (Message message : messages) {
            add(new MessageBox(message));
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        Menu.setMessagesBadge(messagesService.getUncheckedMessages(SessionService.getUserId()).size());
    }
}
