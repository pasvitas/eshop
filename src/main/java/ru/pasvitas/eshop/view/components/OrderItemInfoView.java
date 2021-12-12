package ru.pasvitas.eshop.view.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.pasvitas.eshop.model.OrderItem;
import ru.pasvitas.eshop.service.CartService;

@UIScope
public class OrderItemInfoView extends VerticalLayout {

    private final H2 titleText = new H2();
    private final Span amount = new Span("Количество");
    private final Span priceText = new Span("Цена");

    private final CartService cartService;

    private OrderItem orderItem = null;

    public OrderItemInfoView(CartService cartService, String userName) {
        this.cartService = cartService;
        Button button = new Button("Убрать 1 единицу");
        button.addClickListener(click -> {
            if (orderItem != null) {
                this.cartService.deleteFromCart(userName, orderItem.getItemId());
                Notification.show("Удалено", 3000, Notification.Position.BOTTOM_END);
            }
        });
        add(titleText, amount, priceText, button);
    }

    public void setOrderItem(OrderItem product) {
        orderItem = product;
        if (product == null) {
            setVisible(false);
        }
        else {
            titleText.setText(product.getTitle());
            amount.setText(product.getAmount() + " единиц");
            priceText.setText("Цена:" + product.getPrice() + " д.е.");
            setVisible(true);
        }
    }

}
