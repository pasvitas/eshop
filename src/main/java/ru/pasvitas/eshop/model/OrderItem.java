package ru.pasvitas.eshop.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItem {

    private String itemId;

    private String title;

    private BigDecimal price;

    private int amount;

    public BigDecimal getCost() {
        return price.multiply(BigDecimal.valueOf(amount));
    }
}
