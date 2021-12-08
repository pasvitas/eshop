package ru.pasvitas.eshop.service;

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
import ru.pasvitas.eshop.repository.ProductRepository;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

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
    public long countAllProductsForCategoryName(String category) {
        return productRepository.countAllByCategoryName(category);
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
        productRepository.save(product);
    }

    @Caching(evict = {
            @CacheEvict(value = "products", key = "#productId"),
            @CacheEvict(value = "productsFromCategory", key = "#category")
    })
    @Override
    public void deleteProduct(String productId, String category) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getProducts(int page, int limit) {
        return productRepository.findAll(PageRequest.of(page, limit)).getContent();
    }

    @Override
    public long getProductsCount() {
        return productRepository.count();
    }
}
