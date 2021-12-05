package ru.pasvitas.eshop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import ru.pasvitas.eshop.model.Product;

public interface ProductService {

    List<String> getCategories();
    List<Product> getAllProductsForCategory(String category);

    Optional<Product> getProduct(String id);

    void addProduct(String category, String productName, String description, BigDecimal price);
    void editProduct(String productId, String category, String productName, String description, BigDecimal price);
    void deleteProduct(String productId, String category);
}
