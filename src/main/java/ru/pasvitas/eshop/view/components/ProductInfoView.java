package ru.pasvitas.eshop.view.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.pasvitas.eshop.model.Product;
import ru.pasvitas.eshop.service.CartService;

@UIScope
public class ProductInfoView extends VerticalLayout {

    private final H2 titleText = new H2();
    private final Span descriptionText = new Span("Описание");
    private final Span priceText = new Span("Цена");

    private Product product = null;

    public ProductInfoView(String userName, CartService cartService) {
        add(titleText, descriptionText, priceText);
        if (userName != null) {
            Button button = new Button("Добавить в корзину");
            button.addClickListener(click -> {
                if (product != null) {
                    cartService.addToCart(userName, product.getId());
                    Notification.show("Добавлено", 3000, Notification.Position.BOTTOM_END);
                }
            });
            add(button);
        }
        else {
            add(new Span("Для добавления в корзину войдите в аккаунт"));
        }
    }

    public void setProduct(Product product) {
        this.product = product;
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
