package org.jk.eSked.ui.components.myComponents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.eSked.backend.ApplicationContextHolder;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.MessagesService;

import java.time.LocalDate;

public abstract class MessageBox extends Div {

    private final MessagesService messagesService;

    public MessageBox(Message message) {
        messagesService = ApplicationContextHolder.getContext().getBean(MessagesService.class);

        VerticalLayout leftLayout = getLeftLayout(message);
        VerticalLayout middleLayout = getMiddleLayout(message);
        VerticalLayout rightLayout = getRightLayout(message);

        HorizontalLayout MainLayout = new HorizontalLayout(leftLayout, middleLayout, rightLayout);
        getStyle().set("background", "#304663");
        getStyle().set("border-radius", "5px");

        if (SessionService.isSessionMobile()) {
            MainLayout.setHeight("100px");
            leftLayout.setWidth("100px");
            middleLayout.setWidth("calc(100% - 170px)");
            rightLayout.setWidth("70px");
            setWidth("90vw");
        } else {
            MainLayout.setHeight("100px");
            leftLayout.setWidth("90px");
            middleLayout.setWidth("calc(100% - 150px)");
            rightLayout.setWidth("60px");
            setWidth("60vw");
        }

        add(MainLayout);
    }

    private VerticalLayout getLeftLayout(Message message) {
        LocalDate messageDate = TimeService.InstantToLocalDate(message.getTimestamp());

        String date = messageDate.getDayOfMonth() + ".";
        if (messageDate.getMonthValue() < 10)
            date = date + "0";
        date = date + messageDate.getMonthValue() + "." + messageDate.getYear();

        Label dateText = new Label(date);
        dateText.getStyle().set("margin-top", "auto");
        dateText.getStyle().set("margin-bottom", "auto");

        VerticalLayout left = new VerticalLayout(dateText);
        left.setAlignItems(FlexComponent.Alignment.CENTER);
        left.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return left;
    }

    private VerticalLayout getMiddleLayout(Message message) {
        Label textTitle = new Label("Wiadomość:");

        Label text = new Label(message.getText());
        text.getStyle().set("margin-top", "0px");

        VerticalLayout middle = new VerticalLayout(textTitle, text);
        middle.getStyle().set("margin-left", "0px");
        middle.getStyle().set("padding", "8px");
        return middle;
    }

    private VerticalLayout getRightLayout(Message message) {
        Button deleteButton = new Button(VaadinIcon.CLOSE_SMALL.create(), e -> {
            messagesService.deleteMessage(message.getMessageId());
            refresh();
        });
        deleteButton.setWidth("30px");
        deleteButton.setHeight("30px");
        deleteButton.getStyle().set("color", "red");

        Label checkBoxLabel = new Label("Nowa!");
        checkBoxLabel.getStyle().set("color", "green");
        Checkbox checkbox = new Checkbox();

        VerticalLayout right = new VerticalLayout(checkBoxLabel, checkbox);
        right.setAlignItems(FlexComponent.Alignment.CENTER);
        right.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        right.getStyle().set("margin-left", "0px");

        checkbox.addClickListener(click -> {
            messagesService.setCheckedFlag(message.getMessageId());
            setCheckBox(checkBoxLabel, checkbox);
            right.add(deleteButton);
        });

        if (message.isCheckedFlag()) {
            setCheckBox(checkBoxLabel, checkbox);
            right.add(deleteButton);
        }
        return right;
    }

    private void setCheckBox(Label checkBoxLabel, Checkbox checkbox) {
        checkBoxLabel.setVisible(false);
        checkbox.setValue(true);
        checkbox.setEnabled(false);
    }

    public abstract void refresh();
}
