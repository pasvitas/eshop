package ru.pasvitas.eshop.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.pasvitas.eshop.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> getAllByCategoryName(String categoryName);
}
