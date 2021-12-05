package ru.pasvitas.eshop.service;

import java.util.List;
import ru.pasvitas.eshop.model.Product;

public interface CartService {

    void addToCart(String userId, String productId);
    void deleteFromCart(String userId, String productId);
    List<Product> getProductsFromCart(String userId);
}
