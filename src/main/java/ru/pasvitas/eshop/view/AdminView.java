package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pasvitas.eshop.model.Product;
import ru.pasvitas.eshop.service.ProductService;
import ru.pasvitas.eshop.view.bindermodel.ProductBinderModel;
import ru.pasvitas.eshop.view.callbacks.UpdateFromFormListener;
import ru.pasvitas.eshop.view.forms.ProductForm;

@Route("/admin")
@UIScope
@SpringComponent
@PageTitle("ESHOP | ADMIN")
public class AdminView extends VerticalLayout implements UpdateFromFormListener {

    private final ProductService productService;
    private final ProductsView productsView; //Хреновая стратегия тащить одну вью в другую
    private final Grid<Product> productGrid = new Grid<>();

    @Autowired
    public AdminView(ProductService productService, ProductsView productsView) {
        this.productService = productService;
        this.productsView = productsView;

        productGrid
                .addColumn(Product::getId)
                .setHeader("Идентификатор");

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

                    List<Product> persons = productService.getProducts(offset, limit);

                    return persons.stream();
                },
                count -> (int) productService.getProductsCount()
        ));

        ProductForm productForm = new ProductForm(this.productService, this);

        productGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                productForm.setInitialProduct(null);
            }
            else {
                productForm.setInitialProduct(ProductBinderModel.fromProduct(productGrid.asSingleSelect().getValue()));
            }
        });



        HorizontalLayout horizontalLayout = new HorizontalLayout(productGrid, productForm);
        setSizeFull();
        productGrid.setSizeFull();
        horizontalLayout.setSizeFull();

        //TODO: Уравнять их
        Button addProductButton = new Button("Добавить продукт");
        addProductButton.addClickListener(e -> {
            productGrid.asSingleSelect().clear();
            productForm.setInitialProduct(new ProductBinderModel());
        });

        TextField textField = new TextField();
        textField.setPlaceholder("Название новой категории");

        Button addCategoryButton = new Button("Добавить категорию");
        addCategoryButton.addClickListener(e -> {
            if (!textField.getValue().isEmpty()) {
                productForm.addNewCategory(textField.getValue());
                textField.setValue("");
                Notification.show("Категория добавлена", 5000, Notification.Position.BOTTOM_END)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });

        HorizontalLayout horizontalLayoutButtonAdd = new HorizontalLayout(addProductButton, textField, addCategoryButton);
        add(horizontalLayoutButtonAdd, horizontalLayout);
    }

    @Override
    public void updateData() {
        productGrid.getDataProvider().refreshAll();
        productsView.refreshGrids();
    }
}
