package org.jk.esked.app.frontend.components.other;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.esked.app.backend.model.entities.Message;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.utilities.TimeService;

@CssImport("./styles/message-box.css")
public abstract class MessageBox extends Div {
    private final MessageService messagesService;

    public MessageBox(MessageService messagesService, Message message) {
        this.messagesService = messagesService;

        VerticalLayout leftLayout = getLeftLayout(message);
        leftLayout.addClassName("left-layout");

        VerticalLayout middleLayout = getMiddleLayout(message);
        middleLayout.addClassName("middle-layout");

        VerticalLayout rightLayout = getRightLayout(message);
        rightLayout.addClassName("right-layout");

        add(new HorizontalLayout(leftLayout, middleLayout, rightLayout));
        addClassName("message-box");
    }

    private VerticalLayout getLeftLayout(Message message) {
        String date = TimeService.instantToFormattedDate(message.getTimestamp());
        return new VerticalLayout(new Span(date));
    }

    private VerticalLayout getMiddleLayout(Message message) {
        return new VerticalLayout(
                new Span(getTranslation("message.title")),
                new Span(message.getText()));
    }

    private VerticalLayout getRightLayout(Message message) {
        Button checkButton = new Button(VaadinIcon.CHECK.create(), e -> {
            messagesService.changeCheckedFlagByMessageId(message.getId(), true);
            this.setEnabled(false);
        });

        Button deleteButton = new Button(VaadinIcon.CLOSE_SMALL.create(), e -> {
            messagesService.deleteMessageById(message.getId());
            refresh();
        });
        deleteButton.getStyle().set("color", "red");

        if (message.isCheckedFlag()) checkButton.setEnabled(false);

        return new VerticalLayout(checkButton, deleteButton);
    }

    public abstract void refresh();
}