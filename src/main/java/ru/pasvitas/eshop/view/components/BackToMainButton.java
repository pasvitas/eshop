package ru.pasvitas.eshop.view.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class BackToMainButton extends HorizontalLayout {

    public BackToMainButton() {
        Button button = new Button("Главная");
        button.addClickListener((event) -> UI.getCurrent().navigate("/"));
        add(button);
    }

}
