package ru.pasvitas.eshop.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    private String id;

    private String categoryName;

    private String name;

    private String desc;

    private BigDecimal price;

    private String image;
}
