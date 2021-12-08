package ru.pasvitas.eshop.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.pasvitas.eshop.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, String> {
    List<Product> getAllByCategoryName(String categoryName);

    Page<Product> findAllByCategoryName(String categoryName, Pageable pageable);
    long countAllByCategoryName(String categoryName);
}
