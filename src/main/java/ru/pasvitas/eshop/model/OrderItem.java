package ru.pasvitas.eshop.model;

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

    private String price;

    private int amount;
}
