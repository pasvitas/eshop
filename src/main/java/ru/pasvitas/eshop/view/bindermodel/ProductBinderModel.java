package ru.pasvitas.eshop.view.bindermodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pasvitas.eshop.model.Product;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductBinderModel {
    private String id;

    private String category;

    private String name;

    private String description;

    private String price;

    public static ProductBinderModel fromProduct(Product product) {
        return ProductBinderModel.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategoryName())
                .description(product.getDesc())
                .price(product.getPrice().toString())
                .build();
    }

}
