package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pasvitas.eshop.model.Product;
import ru.pasvitas.eshop.service.ProductService;
import ru.pasvitas.eshop.view.bindermodel.ProductBinderModel;
import ru.pasvitas.eshop.view.callbacks.UpdateFromFormListener;

@Route("")
@SpringComponent
public class AdminView extends VerticalLayout implements UpdateFromFormListener {

    private final ProductService productService;
    private final Grid<Product> productGrid = new Grid<>();

    @Autowired
    public AdminView(ProductService productService) {
        this.productService = productService;

        productGrid
                .addColumn(Product::getId)
                .setHeader("Идентификатор");

        productGrid
                .addColumn(Product::getCategoryName)
                .setHeader("Идентификатор");

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
                count -> productService.getProductsCount()
        ));

        ProductForm productForm = new ProductForm(this.productService, this);

        productGrid.asSingleSelect().addValueChangeListener(event ->
                productForm.setInitialProduct(ProductBinderModel.fromProduct(productGrid.asSingleSelect().getValue())));



        HorizontalLayout horizontalLayout = new HorizontalLayout(productGrid, productForm);
        setSizeFull();
        productGrid.setSizeFull();
        horizontalLayout.setSizeFull();

        Button addProductButton = new Button("Добавить продукт");
        addProductButton.addClickListener(e -> {
            productGrid.asSingleSelect().clear();
            productForm.setInitialProduct(new ProductBinderModel());
        });

        TextField textField = new TextField("Новая категория");

        Button addCategoryButton = new Button("Добавить продукт");
        addCategoryButton.addClickListener(e -> {
            if (!textField.getValue().isEmpty()) {
                productForm.addNewCategory(textField.getValue());
            }
        });

        HorizontalLayout horizontalLayoutButtonAdd = new HorizontalLayout(addProductButton, textField, addCategoryButton);

        add(horizontalLayoutButtonAdd, horizontalLayout);
    }

    @Override
    public void updateData() {
        productGrid.getDataProvider().refreshAll();
    }
}
