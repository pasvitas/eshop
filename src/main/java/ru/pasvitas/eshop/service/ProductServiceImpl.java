package ru.pasvitas.eshop.service;

import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.pasvitas.eshop.model.Product;
import ru.pasvitas.eshop.model.UpdateEvent;
import ru.pasvitas.eshop.model.UpdateType;
import ru.pasvitas.eshop.repository.ProductRepository;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final Subject<UpdateEvent<Product>> updateEventsSubject = PublishSubject.create();

    @Cacheable("categories")
    @Override
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        productRepository
                .findAll()
                .forEach(product -> {
                    if (!categories.contains(product.getCategoryName())) {
                        categories.add(product.getCategoryName());
                    }
                });
        return categories;
    }

    @Cacheable(value = "productsFromCategory", key = "{#category, #page, #size}")
    @Override
    public List<Product> getAllProductsForCategory(String category, int page, int size) {
        return productRepository.findAllByCategoryName(category, PageRequest.of(page, size)).getContent();
    }

    @Override
    public List<Product> searchForItem(String category, String title, int page, int size) {
        return productRepository.findAllByCategoryNameAndNameLike(category, title, PageRequest.of(page, size)).getContent();
    }

    @Override
    public long countAllProductsForCategoryName(String category) {
        return productRepository.countAllByCategoryName(category);
    }

    @Override
    public long countAllProductsForSearchForItem(String category, String title) {
        return productRepository.countAllByCategoryNameAndNameLike(category, title);
    }

    @Cacheable(value = "products", key = "#id")
    @Override
    public Optional<Product> getProduct(String id) {
        return productRepository.findById(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "productsFromCategory", key = "#category")
    })
    @Override
    public void addProduct(String category, String productName, String description, BigDecimal price) {
        Product product = Product.builder()
                .categoryName(category)
                .name(productName)
                .desc(description)
                .price(price)
                .build();
        postUpdate();
        productRepository.save(product);
    }

    @Caching(evict = {
            @CacheEvict(value = "products", key = "#productId"),
            @CacheEvict(value = "productsFromCategory", key = "#category")
    })
    @Override
    public void editProduct(String productId, String category, String productName, String description, BigDecimal price) {
        Product product = Product.builder()
                .id(productId)
                .categoryName(category)
                .name(productName)
                .desc(description)
                .price(price)
                .build();
        postUpdate();
        productRepository.save(product);
    }

    @Caching(evict = {
            @CacheEvict(value = "products", key = "#productId"),
            @CacheEvict(value = "productsFromCategory", key = "#category")
    })
    @Override
    public void deleteProduct(String productId, String category) {
        productRepository.deleteById(productId);
        postUpdate();
    }

    @Override
    public List<Product> getProducts(int page, int limit) {
        return productRepository.findAll(PageRequest.of(page, limit)).getContent();
    }

    @Override
    public long getProductsCount() {
        return productRepository.count();
    }


    @Override
    public Subject<UpdateEvent<Product>> getProductUpdates() {
        return updateEventsSubject;
    }

    private void postUpdate() {
        updateEventsSubject.onNext(new UpdateEvent<>(null, UpdateType.RELOAD_ALL));
    }
}
