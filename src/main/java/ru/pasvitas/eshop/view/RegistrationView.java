package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import ru.pasvitas.eshop.service.UserService;
import ru.pasvitas.eshop.view.components.BackToMainButton;
import ru.pasvitas.eshop.view.forms.RegistrationForm;

@Theme(value = Lumo.class)
@UIScope
@SpringComponent
@Route("/registration")
@PageTitle("ESHOP | Registration")
public class RegistrationView extends VerticalLayout {

    public RegistrationView(UserService userService) {
        RegistrationForm registrationForm = new RegistrationForm(userService);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(new BackToMainButton(), new H1("ESHOP | Регистрация"), registrationForm);
    }
}
