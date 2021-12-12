package ru.pasvitas.eshop.service;

import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pasvitas.eshop.model.Order;
import ru.pasvitas.eshop.model.OrderItem;
import ru.pasvitas.eshop.model.UpdateEvent;
import ru.pasvitas.eshop.model.UpdateType;
import ru.pasvitas.eshop.model.User;
import ru.pasvitas.eshop.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;

    private final ProductService productService;

    private final Subject<UpdateEvent<OrderItem>> updateEventsSubject = PublishSubject.create();

    @Override
    public void addToCart(String userName, String productId) {
        Optional<User> userOptional = userRepository.getUserByUsername(userName);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getCartProductIds().put(productId, user.getCartProductIds().getOrDefault(productId, 0) + 1);
            userRepository.save(user);
            postUpdate();
        }
    }

    @Override
    public void deleteFromCart(String userName, String productId) {
        Optional<User> userOptional = userRepository.getUserByUsername(userName);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getCartProductIds().containsKey(productId)) {
                int currentAmount = user.getCartProductIds().get(productId);
                if (currentAmount > 1) {
                    user.getCartProductIds().put(productId, user.getCartProductIds().get(productId) -1);
                }
                else {
                    user.getCartProductIds().remove(productId);
                }
            }
            userRepository.save(user);
            postUpdate();
        }
    }

    @Override
    public List<OrderItem> getProductsFromCart(String username) {
        List<OrderItem> orderItems = new ArrayList<>();

        userRepository.getUserByUsername(username)
                .map(User::getCartProductIds)
                .orElse(Map.of())
                .forEach((key, value) -> {
                    orderItems.add(productService.getProduct(key)
                            .map(product -> OrderItem.builder()
                                    .itemId(product.getId())
                                    .title(product.getName())
                                    .amount(value)
                                    .price(product.getPrice().toString())
                                    .build()).get());

                });
        return orderItems;
    }

    @Override
    public void convertToOrder(String username, String additionalInfo) {
        Optional<User> user = userRepository.getUserByUsername(username);
        if (user.isPresent()) {
            Order order = Order.builder()
                    .orderItems(getProductsFromCart(username))
                    .details(additionalInfo)
                    .orderDate(new Date())
                    .build();
            user.get().getOrders().add(order);
            user.get().getCartProductIds().clear();
            userRepository.save(user.get());
            postUpdate();
        }
    }

    @Override
    public Subject<UpdateEvent<OrderItem>> getCartUpdates() {
        return updateEventsSubject;
    }

    private void postUpdate() {
        updateEventsSubject.onNext(new UpdateEvent<>(null, UpdateType.RELOAD_ALL));
    }
}
