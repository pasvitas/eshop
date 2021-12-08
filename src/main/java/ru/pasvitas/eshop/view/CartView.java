package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class CartView extends VerticalLayout {

    public CartView() {
        add(new H1("Тут будет текущая корзина. Хотя может и сверху сделать кнопку..."));
    }

}
