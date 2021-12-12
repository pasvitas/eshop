package ru.pasvitas.eshop.service;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.Subject;
import java.util.List;
import ru.pasvitas.eshop.model.OrderItem;
import ru.pasvitas.eshop.model.Product;
import ru.pasvitas.eshop.model.UpdateEvent;

public interface CartService {

    void addToCart(String userName, String productId);
    void deleteFromCart(String userName, String productId);
    List<OrderItem> getProductsFromCart(String username);

    void convertToOrder(String username, String additionalInfo);

    Subject<UpdateEvent<OrderItem>> getCartUpdates();
}
