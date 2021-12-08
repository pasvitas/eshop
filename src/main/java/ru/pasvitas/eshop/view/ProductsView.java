package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.ArrayList;
import java.util.List;
import ru.pasvitas.eshop.model.Product;
import ru.pasvitas.eshop.service.ProductService;

@SpringComponent
@UIScope
public class ProductsView extends VerticalLayout {

    private final ProductService productService;

    private final ProductInfoView productInfoView = new ProductInfoView();

    private List<Grid<Product>> grids = new ArrayList<>();

    public ProductsView(ProductService productService) {
        this.productService = productService;

        Accordion accordion = new Accordion();

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
                .addColumn(Product::getCategoryName)
                .setHeader("Категория");

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
            grid.getDataProvider().refreshAll();
        });
    }
}
