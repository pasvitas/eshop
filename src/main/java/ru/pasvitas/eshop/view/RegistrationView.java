package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import ru.pasvitas.eshop.service.UserService;
import ru.pasvitas.eshop.view.forms.RegistrationForm;

@Theme(value = Lumo.class)
@UIScope
@SpringComponent
@Route("/registration")
public class RegistrationView extends VerticalLayout {

    private final UserService userService;

    private final RegistrationForm registrationForm;

    public RegistrationView(UserService userService) {
        this.userService = userService;
        registrationForm = new RegistrationForm(userService);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(new H1("ESHOP | Регистрация"), registrationForm);
    }
}
