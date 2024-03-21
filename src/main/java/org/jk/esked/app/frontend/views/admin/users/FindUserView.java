package org.jk.esked.app.frontend.views.admin.users;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.admin.AdminReturnButton;
import org.jk.esked.app.frontend.components.admin.UserCreator;
import org.jk.esked.app.frontend.views.MainLayout;

@Route(value = "admin/user", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@CssImport("./styles/admin-styles.css")
class FindUserView extends VerticalLayout implements HasDynamicTitle {
    FindUserView(UserService userService, SecurityService securityService) {
        TextField textField = new TextField(getTranslation("username"));
        textField.focus();

        Button searchForUserButton = new Button(getTranslation("search"), event -> {
            try {
                User user = validateInput(userService, textField.getValue());
                UI.getCurrent().navigate("user/" + user.getId().toString());
            } catch (ValidationException ex) {
                textField.setErrorMessage(ex.getMessage());
                textField.setInvalid(true);
            }
        });
        searchForUserButton.addClickShortcut(Key.ENTER);

        addClassName("find-user-view");
        add(new AdminReturnButton(), textField, searchForUserButton, new UserCreator(userService, securityService));
    }

    private User validateInput(UserService userService, String input) throws ValidationException {
        if (input.isEmpty()) throw new ValidationException(getTranslation("exception.empty.field"));

        User user = userService.findUserByUsername(input);
        if (user == null) throw new ValidationException(getTranslation("exception.user.not.exists"));

        return user;
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.check.user");
    }
}
