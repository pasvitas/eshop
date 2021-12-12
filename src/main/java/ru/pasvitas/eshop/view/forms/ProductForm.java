package ru.pasvitas.eshop.view.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import java.math.BigDecimal;
import java.util.List;
import ru.pasvitas.eshop.service.ProductService;
import ru.pasvitas.eshop.view.bindermodel.ProductBinderModel;

public class ProductForm extends FormLayout {

    private final ProductService productService;

    private final TextField name = new TextField("Название товара");
    private final TextArea description = new TextArea("Описание товара");
    private final TextField price = new TextField("Цена");
    private final ComboBox<String> category = new ComboBox<>("Категория");

    private final Button saveButton = new Button("Сохранить");
    private final Button deleteButton = new Button("Удалить");

    private final Binder<ProductBinderModel> productBinder = new Binder<>(ProductBinderModel.class);

    public ProductForm(ProductService productService) {
        this.productService = productService;
        category.setItems(productService.getCategories());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(category, name, description, price, buttons);

        productBinder.bind(name, ProductBinderModel::getName, ProductBinderModel::setName);
        productBinder.bind(description, ProductBinderModel::getDescription, ProductBinderModel::setDescription);
        productBinder.bind(price, ProductBinderModel::getPrice, ProductBinderModel::setPrice);
        productBinder.bind(category, ProductBinderModel::getCategory, ProductBinderModel::setCategory);

        saveButton.addClickListener((click) -> saveProduct());
        deleteButton.addClickListener((click) -> deleteProduct());

        setInitialProduct(null);
    }

    public void setInitialProduct(ProductBinderModel product) {
        productBinder.setBean(product);
        if (product == null) {
            setVisible(false);
        }
        else {
            setVisible(true);
            category.focus();
        }

    }

    public void addNewCategory(String categoryName) {
        List<String> categories = productService.getCategories();
        categories.add(categoryName);
        category.setItems(categories);
    }

    private void saveProduct() {
        ProductBinderModel product = productBinder.getBean();
        if (product.getId() == null) {
            productService.addProduct(
                    product.getCategory(),
                    product.getName(),
                    product.getDescription(),
                    BigDecimal.valueOf(Double.parseDouble(product.getPrice()))
            );
            Notification.show("Добавлено!", 5000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
        else {
            productService.editProduct(
                    product.getId(),
                    product.getCategory(),
                    product.getName(),
                    product.getDescription(),
                    BigDecimal.valueOf(Double.parseDouble(product.getPrice()))
            );
            Notification.show("Изменено!", 5000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
        setInitialProduct(null);
    }

    private void deleteProduct() {
        ProductBinderModel product = productBinder.getBean();
        if (product.getId() != null) {
            productService.deleteProduct(product.getId(), product.getCategory());
            Notification.show("Удалено!", 5000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
        setInitialProduct(null);
    }

}
