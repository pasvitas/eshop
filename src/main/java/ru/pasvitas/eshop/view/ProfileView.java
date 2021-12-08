package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class ProfileView extends VerticalLayout {

    public ProfileView() {
        add(new H1("Тут будет профиль и заказы пользака"));
    }

}
