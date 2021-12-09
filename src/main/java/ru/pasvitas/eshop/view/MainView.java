package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import ru.pasvitas.eshop.config.security.SecurityChecker;
import ru.pasvitas.eshop.model.UserRole;

@UIScope
@SpringComponent
@Route("")
@PageTitle("ESHOP")
public class MainView extends AppLayout {

    private final ProductsView productsView;
    private final ProfileView profileView;
    private final CartView cartView;

    private final SecurityChecker securityChecker;

    private final Map<Tab, Component> tabsMap = new HashMap<>();

    public MainView(ProductsView productsView, ProfileView profileView, CartView cartView, SecurityChecker securityChecker) {
        this.productsView = productsView;
        this.profileView = profileView;
        this.cartView = cartView;
        this.securityChecker = securityChecker;

        H2 title = new H2("EShop");
        title.addClickListener(click -> {
            Notification.show("Не кликай сюда, ня!", 5000, Notification.Position.BOTTOM_END);
        });


        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), title);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(title);
        header.setWidth("100%");


        UserDetails user = securityChecker.getAuthenticatedUser();
        if (user != null) {
            Span span = new Span(user.getUsername());
            Button logoutButton = new Button("Log out", e -> securityChecker.logout());
            header.add(span, logoutButton);

            if (user.getAuthorities().contains(UserRole.ADMIN)) {
                Button adminButton = new Button("Админка", e -> UI.getCurrent().navigate("/admin"));
                header.add(adminButton);
            }
        }
        else {
            Button loginButton = new Button("Login", e -> UI.getCurrent().navigate("/login"));
            loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            Button registration = new Button("Sign up", e -> UI.getCurrent().navigate("/registration"));
            header.add(loginButton, registration);
        }

        addToNavbar(header);

        Tabs tabs = new Tabs(getProductsViewTab(), getProfileViewTab(), getCartViewTab());
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
