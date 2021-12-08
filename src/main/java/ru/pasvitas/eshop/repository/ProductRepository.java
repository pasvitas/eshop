package ru.pasvitas.eshop.repository;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.pasvitas.eshop.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, String> {
    List<Product> getAllByCategoryName(String categoryName);
}
