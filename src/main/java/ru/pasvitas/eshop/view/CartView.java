package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.reactivex.rxjava3.disposables.Disposable;
import org.springframework.security.core.userdetails.UserDetails;
import ru.pasvitas.eshop.config.security.SecurityChecker;
import ru.pasvitas.eshop.model.OrderItem;
import ru.pasvitas.eshop.service.CartService;
import ru.pasvitas.eshop.view.components.InCartItemView;

@UIScope
@SpringComponent
public class CartView extends VerticalLayout {

    private InCartItemView orderItemInfoView;

    private final CartService cartService;

    private final Grid<OrderItem> cartGrid = new Grid<>();

    private String username = "";

    private Disposable subscriber;

    public CartView(SecurityChecker securityChecker, CartService cartService) {
        this.cartService = cartService;

        UserDetails user = securityChecker.getAuthenticatedUser();
        if (user == null) {
            add(new H1("Для просмотра корзины нужно войти в аккаунт!"));
        }
        else {

            username = user.getUsername();

            add(new H1("Ваша корзина"));
            cartGrid
                    .addColumn(OrderItem::getTitle)
                    .setHeader("Название");

            cartGrid
                    .addColumn(OrderItem::getAmount)
                    .setHeader("Количество");

            cartGrid
                    .addColumn(OrderItem::getPrice)
                    .setHeader("Цена");

            updateGrid();

            orderItemInfoView = new InCartItemView(cartService, user.getUsername());
            orderItemInfoView.setOrderItem(null);
            cartGrid.asSingleSelect().addValueChangeListener(
                    event -> orderItemInfoView.setOrderItem(event.getValue())
            );

            TextArea textArea = new TextArea("Адрес и дополнительная информация:");

            Button buttonMakeOrder = new Button("Сделать заказ", e -> {
                Notification.show("Заказ сделан!", 5000, Notification.Position.BOTTOM_END)
                                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                cartService.convertToOrder(user.getUsername(), textArea.getValue());
                textArea.clear();
            });

            HorizontalLayout horizontalLayout = new HorizontalLayout(cartGrid, orderItemInfoView);
            horizontalLayout.setSizeFull();
            cartGrid.setSizeFull();
            orderItemInfoView.setSizeFull();
            textArea.setSizeFull();
            setSizeFull();
            add(horizontalLayout, textArea, buttonMakeOrder);


        }

    }

    private void updateGrid() {
        UI.getCurrent().access(() -> cartGrid.setItems(
                cartService.getProductsFromCart(username)));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        updateGrid();
        subscriber = cartService.getCartUpdates()
                .subscribe(event -> updateGrid());
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        subscriber.dispose();
    }
}
