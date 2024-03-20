package org.jk.esked.app.frontend.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.backend.model.Message;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.frontend.components.HorizontalLine;
import org.jk.esked.app.frontend.components.MessageBox;

@PermitAll
@Route(value = "messages", layout = MainLayout.class)
public class MessagesView extends VerticalLayout implements HasDynamicTitle {
    private final HorizontalLine horizontalLine = new HorizontalLine();
    private final Text title = new Text(getTranslation("messages"));

    public MessagesView(MessageService messagesService, SecurityService securityService) {
        add(title, horizontalLine);

        renderMessages(messagesService, securityService);
        addClassName("messages-view");
    }

    private void renderMessages(MessageService messagesService, SecurityService securityService) {
        for (Message message : messagesService.getAllMessagesForUserSortedByDate(securityService.getUserId())) {
            add(new MessageBox(messagesService, message) {
                @Override
                public void refresh() {
                    MessagesView.this.removeAll();
                    MessagesView.this.add(title, horizontalLine);
                    renderMessages(messagesService, securityService);
                }
            });
        }
    }

    @Override
    public String getPageTitle() {
        return getTranslation("messages");
    }
}
