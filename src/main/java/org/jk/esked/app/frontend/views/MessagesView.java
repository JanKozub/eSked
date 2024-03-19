package org.jk.esked.app.frontend.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.frontend.components.Line;
import org.jk.esked.app.frontend.components.MessageBox;
import org.jk.esked.app.backend.model.Message;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.TimeService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@PermitAll
@Route(value = "messages", layout = MainLayout.class)
public class MessagesView extends VerticalLayout implements HasDynamicTitle {
    private final MessageService messagesService;
    private final SecurityService securityService;
    private final Line line;
    private final Text title;

    public MessagesView(MessageService messagesService, SecurityService securityService) {
        this.messagesService = messagesService;
        this.securityService = securityService;

        title = new Text(getTranslation("messages"));
        line = new Line();
        line.setWidth("60vw");
        add(title, line);

        renderMessages();

        setAlignItems(Alignment.CENTER);
        setSizeFull();
    }

    private void renderMessages() {
        List<Message> messages = new ArrayList<>(messagesService.getMessagesForUser(securityService.getUserId()));
        messages.sort(Comparator.comparing(sortedMessage -> TimeService.instantToLocalDate(sortedMessage.getTimestamp())));
        for (Message message : messages) {
            add(new MessageBox(messagesService, message) {
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
        return getTranslation("messages");
    }
}
