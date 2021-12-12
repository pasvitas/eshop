package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import ru.pasvitas.eshop.config.security.SecurityChecker;
import ru.pasvitas.eshop.model.Product;
import ru.pasvitas.eshop.service.CartService;
import ru.pasvitas.eshop.service.ProductService;
import ru.pasvitas.eshop.view.components.ProductInfoView;

@SpringComponent
@UIScope
public class ProductsView extends VerticalLayout {

    private final ProductService productService;

    private final ProductInfoView productInfoView;

    private Disposable subscriber;

    private final List<Grid<Product>> grids = new ArrayList<>();

    public ProductsView(ProductService productService, CartService cartService, SecurityChecker securityChecker) {
        this.productService = productService;

        Accordion accordion = new Accordion();

        UserDetails user = securityChecker.getAuthenticatedUser();
        String userName = null;
        if (user != null) {
            userName = user.getUsername();
        }

        this.productInfoView  = new ProductInfoView(userName, cartService);

        for (String category : productService.getCategories()) {
            VerticalLayout categoryLayout = new VerticalLayout(getGridForCategory(category));
            accordion.add(category, categoryLayout);
        }
        accordion.setSizeFull();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        productInfoView.setProduct(null);
        productInfoView.setSizeFull();
        horizontalLayout.add(accordion);
        horizontalLayout.add(productInfoView);
        add(horizontalLayout);
        horizontalLayout.setSizeFull();
        setSizeFull();
    }

    private Grid<Product> getGridForCategory(String category) {

        Grid<Product> productGrid = new Grid<>();

        productGrid
                .addColumn(Product::getName)
                .setHeader("Название");

        productGrid
                .addColumn(Product::getDesc)
                .setHeader("Описание");

        productGrid
                .addColumn(Product::getPrice)
                .setHeader("Цена");

        productGrid.setDataProvider(DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();

                    List<Product> persons = productService.getAllProductsForCategory(category, offset, limit);

                    return persons.stream();
                },
                count -> (int) productService.countAllProductsForCategoryName(category)
        ));

        productGrid.asSingleSelect().addValueChangeListener(
                event -> productInfoView.setProduct(event.getValue())
        );

        grids.add(productGrid);
        return productGrid;
    }

    public void refreshGrids() {
        grids.forEach(grid -> {
            UI.getCurrent().access(() -> grid.getDataProvider().refreshAll());
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        refreshGrids();
        subscriber = productService.getProductUpdates()
                .subscribe(event -> refreshGrids());
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        subscriber.dispose();
    }
}
