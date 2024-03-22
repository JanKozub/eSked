package org.jk.esked.app.frontend.views.other;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.frontend.components.other.HorizontalLine;
import org.jk.esked.app.frontend.components.other.MessageBox;
import org.jk.esked.app.frontend.views.MainLayout;

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
        messagesService.getAllMessagesForUserSortedByDate(securityService.getUserId())
                .forEach(m -> add(new MessageBox(messagesService, m) {
                    @Override
                    public void refresh() {
                        MessagesView.this.removeAll();
                        MessagesView.this.add(title, horizontalLine);
                        renderMessages(messagesService, securityService);
                    }
                }));
    }

    @Override
    public String getPageTitle() {
        return getTranslation("messages");
    }
}
