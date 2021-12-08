package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.pasvitas.eshop.model.Product;

@UIScope
public class ProductInfoView extends VerticalLayout {

    private final H2 titleText = new H2();
    private final Span descriptionText = new Span("Описание");
    private final Span priceText = new Span("Цена");


    public ProductInfoView() {
        Button button = new Button("Добавить в корзину");
        button.addClickListener(click -> Notification.show("Типа добавлено, но нет", 5000, Notification.Position.BOTTOM_END));
        add(titleText, descriptionText, priceText, button);
    }

    public void setProduct(Product product) {
        if (product == null) {
            setVisible(false);
        }
        else {
            titleText.setText(product.getName());
            descriptionText.setText(product.getDesc());
            priceText.setText("Цена:" + product.getPrice() + " д.е.");
            setVisible(true);
        }
    }

}
