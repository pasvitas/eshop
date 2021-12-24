package ru.pasvitas.eshop.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
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
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pasvitas.eshop.model.Product;
import ru.pasvitas.eshop.service.ProductService;
import ru.pasvitas.eshop.view.bindermodel.ProductBinderModel;
import ru.pasvitas.eshop.view.components.BackToMainButton;
import ru.pasvitas.eshop.view.forms.ProductForm;

@Route("/admin")
@UIScope
@SpringComponent
@PageTitle("ESHOP | Admin")
public class AdminView extends AppLayout {

    private final Grid<Product> productGrid = new Grid<>();

    private Disposable subscriber;

    private final ProductService productService;

    @Autowired
    public AdminView(ProductService productService) {
        this.productService = productService;
        productGrid
                .addColumn(Product::getId)
                .setHeader("Идентификатор");

        productGrid
                .addColumn(Product::getCategoryName)
                .setHeader("Категория");

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

                    List<Product> persons = this.productService.getProducts(offset, limit);

                    return persons.stream();
                },
                count -> (int) this.productService.getProductsCount()
        ));

        ProductForm productForm = new ProductForm(this.productService);

        productGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                productForm.setInitialProduct(null);
            }
            else {
                productForm.setInitialProduct(ProductBinderModel.fromProduct(productGrid.asSingleSelect().getValue()));
            }
        });



        HorizontalLayout horizontalLayout = new HorizontalLayout(productGrid, productForm);
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
        VerticalLayout verticalLayout = new VerticalLayout(horizontalLayoutButtonAdd, horizontalLayout);
        verticalLayout.setSizeFull();
        setContent(verticalLayout);

        addToNavbar(new BackToMainButton());

    }

    private void updateGrid() {
        UI.getCurrent().access(() -> productGrid.getDataProvider().refreshAll());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        subscriber = productService.getProductUpdates()
                .subscribe(event -> updateGrid());
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        subscriber.dispose();
    }
}
