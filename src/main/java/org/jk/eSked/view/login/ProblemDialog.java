package org.jk.eSked.view.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

class ProblemDialog extends Dialog {

    ProblemDialog() {

        Icon passIcon = new Icon(VaadinIcon.CLOSE_CIRCLE);
        Label passLabel = new Label("Odzyskaj Hasło");
        passLabel.getStyle().set("font-weight", "bold");
        TextField passField = new TextField();
        passField.setPlaceholder("Nazwa Użytkownika");
        Button passButton = new Button("Wyślij");
        HorizontalLayout passLabelLayout = new HorizontalLayout(passIcon, passLabel);
        HorizontalLayout passFieldLayout = new HorizontalLayout(passField, passButton);
        passFieldLayout.setWidth("100%");

        Icon emailIcon = new Icon(VaadinIcon.ENVELOPE);
        Label emailLabel = new Label("Kontakt");
        emailLabel.getStyle().set("font-weight", "bold");
        Label email = new Label("info.schedule@gmail.com");
        email.getStyle().set("font-weight", "bold");
        HorizontalLayout emailLayout = new HorizontalLayout(emailIcon, emailLabel);

        VerticalLayout mainLayout = new VerticalLayout(passLabelLayout, passFieldLayout, emailLayout, email);
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(mainLayout);
    }
}
