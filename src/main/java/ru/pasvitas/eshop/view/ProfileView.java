package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import ru.pasvitas.eshop.config.security.SecurityChecker;
import ru.pasvitas.eshop.model.Order;
import ru.pasvitas.eshop.model.OrderItem;
import ru.pasvitas.eshop.service.ProfileService;

@UIScope
@SpringComponent
public class ProfileView extends VerticalLayout {

    private final ProfileService profileService;

    private final SecurityChecker securityChecker;

    private final Map<String, Grid<OrderItem>> grids = new HashMap<>();

    private static final DateFormat DATE_ORDER_FORMATTER = new SimpleDateFormat("dd MM yyyy HH:mm");

    public ProfileView(ProfileService profileService, SecurityChecker securityChecker) {
        this.profileService = profileService;
        this.securityChecker = securityChecker;
        rebuildView();
    }

    private Grid<OrderItem> getGridForOrder(Order order) {

        Grid<OrderItem> ordersGrid = new Grid<>();

        ordersGrid
                .addColumn(OrderItem::getTitle)
                .setHeader("Название");

        ordersGrid
                .addColumn(OrderItem::getPrice)
                .setHeader("Цена");

        ordersGrid
                .addColumn(OrderItem::getAmount)
                .setHeader("Количество");

        ordersGrid
                .addColumn(OrderItem::getCost)
                .setHeader("Конечная стоимость");

        ordersGrid.setItems(order.getOrderItems());
        return ordersGrid;
    }

    private void rebuildView() {

        add(new H1("История заказов"));

        removeAll();

        Accordion accordion = new Accordion();

        UserDetails user = securityChecker.getAuthenticatedUser();
        if (user != null) {
            List<Order> getOrders = profileService.getOrders(user.getUsername());
            getOrders.forEach(order -> {
                VerticalLayout verticalLayout = new VerticalLayout();
                Span span = new Span(order.getDetails());
                verticalLayout.setSizeFull();
                verticalLayout.add(span, getGridForOrder(order));
                accordion.add(DATE_ORDER_FORMATTER.format(order.getOrderDate()), verticalLayout);
            });
        }
        else {
            add(new H2("Вам нужно авторизоваться для просмотра заказов!"));
        }

        accordion.setSizeFull();
        add(accordion);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        rebuildView();
    }

}
