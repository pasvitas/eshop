package ru.pasvitas.eshop.model;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    private Date orderDate;

    private String details;

    private List<OrderItem> orderItems;

}
