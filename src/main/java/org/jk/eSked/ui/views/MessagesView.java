package org.jk.eSked.ui.views;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.MessagesService;
import org.jk.eSked.ui.components.myComponents.Line;
import org.jk.eSked.ui.components.myComponents.MessageBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Route(value = "messages", layout = MainLayout.class)
public class MessagesView extends VerticalLayout implements HasDynamicTitle {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private final MessagesService messagesService;
    private final Line line;
    private final Label title;

    public MessagesView(MessagesService messagesService) {
        this.messagesService = messagesService;
        SessionService.setAutoTheme();

        title = new Label(getTranslation(locale, "messages"));
        line = new Line();
        if (SessionService.isSessionMobile())
            line.setWidth("90vw");
        else
            line.setWidth("60vw");
        add(title, line);

        renderMessages();

        setAlignItems(Alignment.CENTER);
        setSizeFull();
    }

    private void renderMessages() {
        List<Message> messages = new ArrayList<>(messagesService.getMessagesForUser(SessionService.getUserId()));
        messages.sort(Comparator.comparing(sortedMessage -> TimeService.InstantToLocalDate(sortedMessage.getTimestamp())));
        for (Message message : messages) {
            add(new MessageBox(message) {
                @Override
                public void refresh() {
                    MessagesView.this.removeAll();
                    MessagesView.this.add(title, line);
                    renderMessages();
                }
            });
        }
    }

    @Override
    public String getPageTitle() {
        return getTranslation(locale, "messages");
    }
}
