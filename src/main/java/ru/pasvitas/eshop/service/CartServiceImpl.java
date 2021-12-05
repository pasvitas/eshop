package ru.pasvitas.eshop.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pasvitas.eshop.model.Product;
import ru.pasvitas.eshop.model.User;
import ru.pasvitas.eshop.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;

    private final ProductService productService;

    @Override
    public void addToCart(String userId, String productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getCartProductIds().add(productId);
            userRepository.save(user);
        }
    }

    @Override
    public void deleteFromCart(String userId, String productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getCartProductIds().remove(productId);
            userRepository.save(user);
        }
    }

    @Override
    public List<Product> getProductsFromCart(String userId) {
        return userRepository.findById(userId)
                .map(user -> user
                .getCartProductIds()
                .stream()
                .map(id -> productService.getProduct(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()))
                .orElseGet(List::of);
    }
}
