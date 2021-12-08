package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.HashMap;
import java.util.Map;

@UIScope
@SpringComponent
@Route("")
public class MainView extends AppLayout {

    private final AdminView adminView;
    private final ProductsView productsView;
    private final ProfileView profileView;
    private final CartView cartView;

    private final Map<Tab, Component> tabsMap = new HashMap<>();

    public MainView(AdminView adminView, ProductsView productsView, ProfileView profileView, CartView cartView) {
        this.adminView = adminView;
        this.productsView = productsView;
        this.profileView = profileView;
        this.cartView = cartView;

        H2 title = new H2("EShop");
        title.addClickListener(click -> {
            Notification.show("Не кликай сюда, ня!", 5000, Notification.Position.BOTTOM_END);
        });

        addToNavbar(new DrawerToggle(), title);

        Tabs tabs = new Tabs(getProductsViewTab(), getProfileViewTab(), getCartViewTab(), getAdminViewTab());
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final Component component = tabsMap.get(selectedTab);
            setContent(component);
        });
        tabs.setSelectedIndex(0);
        setContent(tabsMap.get(tabs.getSelectedTab()));
        addToDrawer(tabs);
    }


    private Tab getProductsViewTab() {
        final Span label = new Span("Товары");
        final Icon icon = VaadinIcon.SHOP.create();
        final Tab tab = new Tab(icon, label);
        tabsMap.put(tab, productsView);
        return tab;
    }

    private Tab getAdminViewTab() {
        final Span label = new Span("Админка");
        final Icon icon = VaadinIcon.TOOLS.create();
        final Tab tab = new Tab(icon, label);
        tabsMap.put(tab, adminView);
        return tab;
    }

    private Tab getCartViewTab() {
        final Span label = new Span("Корзина");
        final Icon icon = VaadinIcon.CART.create();
        final Tab tab = new Tab(icon, label);
        tabsMap.put(tab, cartView);
        return tab;
    }

    private Tab getProfileViewTab() {
        final Span label = new Span("Профиль");
        final Icon icon = VaadinIcon.USER.create();
        final Tab tab = new Tab(icon, label);
        tabsMap.put(tab, profileView);
        return tab;
    }

}
