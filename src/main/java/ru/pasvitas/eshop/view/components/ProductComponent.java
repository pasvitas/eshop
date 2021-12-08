package ru.pasvitas.eshop.view.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.pasvitas.eshop.model.Product;
import ru.pasvitas.eshop.view.callbacks.BuyButtonListener;

@UIScope
public class ProductComponent extends VerticalLayout {

    public ProductComponent(Product product, BuyButtonListener buyButtonListener) {
        Text titleText = new Text(product.getName());
        Text descriptionText = new Text(product.getDesc());
        Text priceText = new Text(product.getPrice().toString());
        Button buyButton = new Button("Купить");
        buyButton.addClickListener(click -> buyButtonListener.buyButtonClicked(product));

        add(titleText, descriptionText, priceText, buyButton);
    }

}
