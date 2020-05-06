package org.jk.eSked.ui.components.myComponents;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.eSked.backend.ApplicationContextHolder;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.MessagesService;

import java.time.LocalDate;

public class MessageBox extends Div {

    private final MessagesService messagesService;

    public MessageBox(Message message) {
        messagesService = ApplicationContextHolder.getContext().getBean(MessagesService.class);
        LocalDate messageDate = TimeService.InstantToLocalDate(message.getTimestamp());
        Label dateLabel = new Label(messageDate.getDayOfMonth() + "."
                + messageDate.getMonthValue() + "."
                + messageDate.getYear());
        dateLabel.getStyle().set("margin-left", "4%");

        Label textTitle = new Label("Wiadomość:");
        textTitle.getStyle().set("margin-top", "0px");
        Label text = new Label(message.getText());
        text.getStyle().set("margin-top", "0px");
        VerticalLayout textLayout = new VerticalLayout(textTitle, text);
        textLayout.getStyle().set("margin-left", "5%");
        Label checkBoxLabel = new Label("Nowa!");
        checkBoxLabel.getStyle().set("color", "green");
        Checkbox checkbox = new Checkbox();
        checkbox.addClickListener(click -> {
            messagesService.setCheckedFlag(message.getMessageId());
            checkBoxLabel.setVisible(false);
            checkbox.setValue(true);
            checkbox.setEnabled(false);
        });

        VerticalLayout isSeenLayout = new VerticalLayout(checkBoxLabel, checkbox);
        isSeenLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        isSeenLayout.getStyle().set("margin-left", "auto");
        isSeenLayout.getStyle().set("padding-right", "2%");
        isSeenLayout.setWidth("20%");
        if (message.isCheckedFlag()) {
            checkBoxLabel.setVisible(false);
            checkbox.setValue(true);
            checkbox.setEnabled(false);
        }

        HorizontalLayout layout = new HorizontalLayout(dateLabel, textLayout, isSeenLayout);
        layout.setSizeFull();
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(layout);
        setCss();
    }

    private void setCss() {
        if (SessionService.isSessionMobile()) {
            setWidth("100%");
            setHeight("20%");
            setMinHeight("20%");
        } else {
            setWidth("50%");
            setHeight("10%");
            setMinHeight("10%");
        }
        getStyle().set("background", "#304663");
        getStyle().set("border-radius", "5px");
    }
}
